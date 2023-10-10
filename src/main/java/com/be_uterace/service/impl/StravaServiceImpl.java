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
        headers.set("Content-Type", "application/json");
        HttpEntity<String> request = new HttpEntity<String>("parameters", headers);
        RestTemplate restTemplate = new RestTemplate();
        if (code != null) {
            StravaOauthResponse strava = restTemplate.exchange(urlOauth+params, HttpMethod.POST, request, StravaOauthResponse.class).getBody();
            assert strava != null;
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = userRepository.findByUsername(authentication.getName()).orElse(null);
            if (user.getStravaId() == null) {
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
            return ConnectStravaResponse.builder()
                    .detail("Connect Strava Success")
                    .stravaId(user.getStravaId())
                    .stravaFullname(user.getStravaFullName())
                    .stravaImage(user.getStravaImage())
                    .build();
        }
        return null;
    }
}
