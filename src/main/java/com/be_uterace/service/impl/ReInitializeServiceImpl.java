package com.be_uterace.service.impl;

import com.be_uterace.entity.Run;
import com.be_uterace.entity.User;
import com.be_uterace.exception.BadRequestException;
import com.be_uterace.exception.ResourceNotFoundException;
import com.be_uterace.payload.response.ResponseObject;
import com.be_uterace.payload.response.stravaresponse.ActivityStravaResponse;
import com.be_uterace.repository.RunRepository;
import com.be_uterace.repository.UCActivityRepository;
import com.be_uterace.repository.UEActivityRepository;
import com.be_uterace.repository.UserRepository;
import com.be_uterace.service.ReInitializeService;
import com.be_uterace.service.StravaService;
import com.be_uterace.utils.StatusCode;
import com.be_uterace.utils.VariableGlobal;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.Query;
import jakarta.persistence.StoredProcedureQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static com.be_uterace.utils.Format.formatSeconds;
import static com.be_uterace.utils.StravaUtils.getAllActivities;
import static com.be_uterace.utils.StravaUtils.refreshStravaToken;
import static com.be_uterace.utils.Validation.checkPace;

@Transactional
@Service
public class ReInitializeServiceImpl implements ReInitializeService {
    private ApplicationContext applicationContext;
    private UCActivityRepository ucActivityRepository;
    private UEActivityRepository ueActivityRepository;
    private RunRepository runRepository;
    private UserRepository userRepository;

    private EntityManager entityManager;



    public ReInitializeServiceImpl(UCActivityRepository ucActivityRepository,
                                   UEActivityRepository ueActivityRepository,
                                   RunRepository runRepository,
                                   UserRepository userRepository,
                                   EntityManager entityManager){
        //ApplicationContext applicationContext) {
        this.ucActivityRepository = ucActivityRepository;
        this.ueActivityRepository = ueActivityRepository;
        this.runRepository = runRepository;
        this.userRepository = userRepository;
        this.entityManager = entityManager;
        //this.applicationContext = applicationContext;

    }

    @Override
    public ResponseObject reInitializeAll()  {
        if (VariableGlobal.getFlag()==true) {
            return new ResponseObject(StatusCode.SUCCESS, "Vui lòng chờ đợi đồng bộ dữ liệu hoàn tất");
        } else {

            VariableGlobal.setFlag(true);
            List<User> userList = userRepository.findAllByStravaIdIsNotNull();;
            callPostgresFunctionForUserList(userList);
            CompletableFuture.runAsync(() -> {
                try {
                    userList.forEach(user -> user.setSyncStatus("0"));
                    userRepository.saveAll(userList);
                    for (User user : userList) {
                        re_initialize_activity(user);
                    }

                } catch (IOException e) {
                    userList.forEach(user -> user.setSyncStatus("-1"));
                    userRepository.saveAll(userList);
                    throw new RuntimeException(e);
                }
                finally {
                    VariableGlobal.setFlag(false);
                }
            });
            return new ResponseObject(StatusCode.SUCCESS, "Đang bắt đầu quá trình đồng bộ dữ liệu");
        }
    }



    @Override
    public ResponseObject reInitializeForUser(Long userId) {
        Optional<User> userOptional = Optional.ofNullable(userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User")));
        User user = userOptional.get();

        if (user.getStravaId() == null) {
            throw new BadRequestException(HttpStatus.BAD_REQUEST, "Người dùng chưa kết nối tới Strava");
        }



        if ("0".equals(user.getSyncStatus())) {
            return new ResponseObject(StatusCode.SUCCESS, "Vui lòng chờ đợi đồng bộ dữ liệu hoàn tất");
        } else {
            Long[] userIdsArray = new Long[]{userId};
            runRepository.deleteUsersData(userIdsArray);
            CompletableFuture.runAsync(() -> {
                try {
                    re_initialize_activity(user);
                } catch (IOException e) {
                    user.setSyncStatus("-1");
                    throw new RuntimeException(e);
                }

                user.setSyncStatus("1");
                userRepository.save(user);
            });

            // Avoid unnecessary userRepository.save(user) here
            user.setSyncStatus("0");

            return new ResponseObject(StatusCode.SUCCESS, "Đã bắt đầu quá trình đồng bộ dữ liệu cho user");
        }
    }

    public void re_initialize_activity(User user) throws IOException {
        addRuns(user,user.getStravaAccessToken());
        LocalDateTime currentTime = LocalDateTime.now();
        user.setSyncStatus("1");
        user.setLast_sync(Timestamp.valueOf(currentTime));
        userRepository.save(user);
    }

    public void addRuns(User user, String accessToken) throws IOException {
        List<ActivityStravaResponse> activities = getAllActivities(accessToken);
        if(activities==null){
            Map<String, String> res = refreshStravaToken(user.getStravaRefreshToken());
            user.setStravaAccessToken(res.get("access_token"));
            user.setStravaRefreshToken(res.get("refresh_token"));
            activities = getAllActivities(res.get("access_token"));
            userRepository.save(user);
        }

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

    public void callPostgresFunctionForUserList(List<User> userList) {
        List<Long> userIdsList = userList.stream()
                .map(User::getUserId)
                .collect(Collectors.toList());
        Long[] userIdsArray = userIdsList.toArray(new Long[0]);
        runRepository.deleteUsersData(userIdsArray);
    }
}
