package com.be_uterace.service.impl;

import com.be_uterace.entity.Run;
import com.be_uterace.entity.User;
import com.be_uterace.payload.response.ConnectStravaResponse;
import com.be_uterace.payload.response.ResponseObject;
import com.be_uterace.payload.response.stravaresponse.ActivityStravaResponse;
import com.be_uterace.payload.response.stravaresponse.StravaOauthResponse;
import com.be_uterace.repository.RunRepository;
import com.be_uterace.repository.UserRepository;
import com.be_uterace.service.StravaService;
import com.be_uterace.utils.StatusCode;
import com.be_uterace.utils.StravaUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.be_uterace.utils.Format;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;

import static com.be_uterace.utils.Format.formatSeconds;
import static com.be_uterace.utils.StravaUtils.getAllActivities;
import static com.be_uterace.utils.Validation.checkPace;

@Service
public class StravaServiceImpl implements StravaService {
    @Value("${strava.client_id}")
    private String clientId;
    @Value("${strava.client_secret}")
    private String clientSecret;
    @Value("${strava.grant_type}")
    private String grantType;
    private UserRepository userRepository;
    private RunRepository runRepository;

    public StravaServiceImpl(UserRepository userRepository, RunRepository runRepository) {
        this.userRepository = userRepository;
        this.runRepository = runRepository;
    }

    @Override
    public ConnectStravaResponse connectStrava(String code) throws IOException {
        if (code != null) {
            StravaOauthResponse stravaOauthResponse = StravaUtils.exchangeAuthorizationCode(code);
            assert stravaOauthResponse != null;
            //strava này đã được kết nối bởi người khác
            if (userRepository.existsByStravaId(stravaOauthResponse.getAthlete().getId())) {
                return ConnectStravaResponse.builder()
                        .status(201)
                        .detail("Strava has been connected by another account")
                        .stravaFullname(null)
                        .stravaImage(null)
                        .stravaId(null)
                        .build();
            }
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = userRepository.findByUsername(authentication.getName()).orElse(null);
            assert user != null;
            //Kiểm tra user đã có kết nối với strava hay chưa
            if (user.getStravaId() !=null) {
                return ConnectStravaResponse.builder()
                        .detail("Strava has been connected")
                        .stravaId(stravaOauthResponse.getAthlete().getId())
                        .stravaFullname(stravaOauthResponse.getAthlete().getFullName())
                        .stravaImage(stravaOauthResponse.getAthlete().getProfile())
                        .build();
            }
            //Thêm vào database nếu user chưa kết nối strava
            addRuns(user,stravaOauthResponse.getAccess_token());
            //
            user.setStravaId(stravaOauthResponse.getAthlete().getId());
            user.setStravaAccessToken(stravaOauthResponse.getAccess_token());
            user.setStravaRefreshToken(stravaOauthResponse.getRefresh_token());
            user.setStravaFullName(stravaOauthResponse.getAthlete().getFullName());
            user.setStravaImage(stravaOauthResponse.getAthlete().getProfile());
            userRepository.save(user);
            return ConnectStravaResponse.builder()
                    .status(200)
                    .detail("Connect Strava Success")
                    .stravaId(stravaOauthResponse.getAthlete().getId())
                    .stravaFullname(stravaOauthResponse.getAthlete().getFullName())
                    .stravaImage(stravaOauthResponse.getAthlete().getProfile())
                    .build();
        }
        return ConnectStravaResponse.builder()
                .status(400)
                .detail("Connect Strava Fail")
                .stravaFullname(null)
                .stravaImage(null)
                .stravaId(null)
                .build();
    }

    @Override
    public ConnectStravaResponse disconnectStrava() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName()).orElse(null);
        assert user != null;
        user.setStravaId(null);
        user.setStravaAccessToken(null);
        user.setStravaRefreshToken(null);
        user.setStravaFullName(null);
        user.setStravaImage(null);
        userRepository.save(user);
        removeRuns();
        return ConnectStravaResponse.builder()
                .detail("Disconnect Strava Success")
                .stravaId(null)
                .stravaFullname(null)
                .stravaImage(null)
                .build();
    }

    @Override
    public ConnectStravaResponse statusStrava() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName()).orElse(null);
        assert user != null;
        if (user.getStravaId() != null) {
            return ConnectStravaResponse.builder()
                    .detail("Strava has been connected")
                    .stravaId(user.getStravaId())
                    .stravaFullname(user.getStravaFullName())
                    .stravaImage(user.getStravaImage())
                    .build();
        }
        return ConnectStravaResponse.builder()
                .detail("Strava has not been connected")
                .stravaId(null)
                .stravaFullname(null)
                .stravaImage(null)
                .build();
    }

    public void addRuns(User user, String accessToken) {
        List<ActivityStravaResponse> activities = getAllActivities(accessToken);

        List<Run> runsToSave = new ArrayList<>();

        for (ActivityStravaResponse activity : activities) {
            boolean runCheck = runRepository.existsByStravaRunId(activity.getId());

            // If runCheck is true, skip to the next iteration
            if (runCheck) {
                continue;
            }

            Date start_date_local = activity.getStart_date_local();
            //assert start_date_local.before(user.getCreatedAt());
            Double averageSpeed = activity.getAverage_speed();
            double pace = (averageSpeed != null && averageSpeed != 0) ? 60 / (averageSpeed * 3.6) : 0;

            Run run = new Run();
            run.setUser(user);
            run.setStravaRunId(activity.getId());
            run.setName(activity.getName());
            run.setDistance(activity.getDistance()/1000);
            run.setType(activity.getType());
            run.setDuration(formatSeconds(activity.getMoving_time()));
            run.setPace(pace);
            run.setStatus(checkPace(pace));
            run.setStepRate(Objects.equals(activity.getStep_rate(), null) ? activity.getStep_rate() : null);
            run.setHeartRate(Objects.equals(activity.getAverage_heartrate(), null) ? activity.getAverage_heartrate() : null);
            run.setCalori(Objects.equals(activity.getCalories(), null) ? activity.getCalories() : null);
            run.setCreatedAt(start_date_local);

            String summaryPolyline = activity.getMap().getSummary_polyline();
            run.setSummaryPolyline(summaryPolyline != null ? summaryPolyline : null);

            runsToSave.add(run);
        }

        // Save all runs in a batch
        runRepository.saveAll(runsToSave);
    }
     public void removeRuns() {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
         if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)){
             return;
         }
         UserDetails userDetails = (UserDetails) authentication.getPrincipal();
         String username = userDetails.getUsername();
         Optional<User> userOptional = userRepository.findByUsername(username);
         if (userOptional.isEmpty()) {
             return;
         }
         User user = userOptional.get();
         List<Run> runs = runRepository.findAllByUser_UserId(user.getUserId());
         runRepository.deleteAll(runs);
     }
}
