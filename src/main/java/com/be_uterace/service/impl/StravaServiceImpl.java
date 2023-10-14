package com.be_uterace.service.impl;

import com.be_uterace.entity.User;
import com.be_uterace.payload.response.ConnectStravaResponse;
import com.be_uterace.payload.response.StravaOauthResponse;
import com.be_uterace.repository.UserRepository;
import com.be_uterace.service.StravaService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;
@Service
public class StravaServiceImpl implements StravaService {
    @Value("${strava.client_id}")
    private String clientId;
    @Value("${strava.client_secret}")
    private String clientSecret;
    @Value("${strava.grant_type}")
    private String grantType;
    private UserRepository userRepository;
    public StravaServiceImpl (UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public ConnectStravaResponse connectStrava(String code) {
        String urlOauth = "https://www.strava.com/api/v3/oauth/token";
        String params = "?client_id="+clientId+"&client_secret="+clientSecret+"&code="+code+"&grant_type="+grantType;
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> request = new HttpEntity<String>("parameters", headers);
        RestTemplate restTemplate = new RestTemplate();
        if (code != null) {
            StravaOauthResponse strava = restTemplate.exchange(urlOauth+params, HttpMethod.POST, request, StravaOauthResponse.class).getBody();
            assert strava != null;
            boolean userExistsWithStravaId = userRepository.existsByStravaId(strava.getAthlete().getId());
            if (userExistsWithStravaId) {
                return ConnectStravaResponse.builder()
                        .detail("Strava has been connected by another account")
                        .build();
            }
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = userRepository.findByUsername(authentication.getName()).orElse(null);
            assert user != null;
            if (user.getStravaId() !=null) {
                return ConnectStravaResponse.builder()
                        .detail("Strava has been connected")
                        .stravaId(strava.getAthlete().getId())
                        .stravaFullname(strava.getAthlete().getFullName())
                        .stravaImage(strava.getAthlete().getProfile())
                        .build();
            }
            user.setStravaId(strava.getAthlete().getId());
            user.setStravaAccessToken(strava.getAccess_token());
            user.setStravaRefreshToken(strava.getRefresh_token());
            user.setStravaFullName(strava.getAthlete().getFullName());
            user.setStravaImage(strava.getAthlete().getProfile());
            userRepository.save(user);
            return ConnectStravaResponse.builder()
                    .detail("Connect Strava Success")
                    .stravaId(strava.getAthlete().getId())
                    .stravaFullname(strava.getAthlete().getFullName())
                    .stravaImage(strava.getAthlete().getProfile())
                    .build();
        }
        return null;
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
        return ConnectStravaResponse.builder()
                .detail("Disconnect Strava Success")
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
                .build();
    }
}
