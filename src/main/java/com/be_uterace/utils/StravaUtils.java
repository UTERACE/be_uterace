package com.be_uterace.utils;

import java.io.IOException;
import java.util.*;

import com.be_uterace.payload.response.stravaresponse.ActivityStravaResponse;
import com.be_uterace.payload.response.stravaresponse.AthleteResponse;
import com.be_uterace.payload.response.stravaresponse.StravaOauthResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import okhttp3.*;
import com.google.gson.reflect.TypeToken;
public class StravaUtils {
    private static final String CLIENT_ID = "110763";
    private static final String CLIENT_SECRET = "d5ce586f678bf958731f5b8371dbe69ce27c061e";
    private static final Gson gson = new Gson();
    private static final OkHttpClient client = new OkHttpClient();
    private static final ObjectMapper objectMapper = new ObjectMapper();


    public static List<ActivityStravaResponse> getAllActivities(String accessToken) {
        String url = "https://www.strava.com/api/v3/athlete/activities";
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + accessToken)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String jsonResponse = response.body().string();
                return new Gson().fromJson(jsonResponse, new TypeToken<List<ActivityStravaResponse>>() {}.getType());
            } else {
                return null; // or throw a specific exception
            }
        } catch (IOException e) {
            return null; // or throw a specific exception
        }
    }

    public static String getAllActivitiesAt(String accessToken) throws IOException {
        String url = "https://www.strava.com/api/v3/athlete/activities";
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + accessToken)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful() ? response.body().string() : null;
        }
    }

    public static StravaOauthResponse exchangeAuthorizationCode(String authorizationCode) throws IOException {
        String url = "https://www.strava.com/oauth/token";
        FormBody formBody = new FormBody.Builder()
                .add("client_id", CLIENT_ID)
                .add("client_secret", CLIENT_SECRET)
                .add("code", authorizationCode)
                .add("grant_type", "authorization_code")
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return gson.fromJson(response.body().string(), StravaOauthResponse.class);
        }
    }

    public static String exchangeAuthorizationCodeAt(String authorizationCode) throws IOException {
        String url = "https://www.strava.com/oauth/token";
        FormBody formBody = new FormBody.Builder()
                .add("client_id", CLIENT_ID)
                .add("client_secret", CLIENT_SECRET)
                .add("code", authorizationCode)
                .add("grant_type", "authorization_code")
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            Map<String, String> data = gson.fromJson(response.body().string(), Map.class);
            return data.get("access_token");
        }
    }

//    public static Map<String, String> revokeAccessToken(String accessToken) throws IOException {
//        String url = "https://www.strava.com/oauth/deauthorize";
//        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
//        urlBuilder.addQueryParameter("access_token", accessToken);
//        String finalUrl = urlBuilder.build().toString();
//
//        Request request = new Request.Builder()
//                .url(finalUrl)
//                .post(RequestBody.create(new byte[0]))
//                .build();
//
//        try (Response response = client.newCall(request).execute()) {
//            Map<String, String> result = new HashMap<>();
//            result.put("message", response.isSuccessful() ? "Hủy kết nối thành công" : "Hủy kết nối thất bại");
//            return result;
//        }
//    }

    public static String getActivityInfoById(Long objectId, String accessToken) throws IOException {
        String url = "https://www.strava.com/api/v3/activities/" + objectId;
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + accessToken)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful() ? response.body().string() : null;
        }
    }

    public static Map<String, String> refreshStravaToken(String refreshToken) throws IOException {
        String url = "https://www.strava.com/api/v3/oauth/token";
        FormBody formBody = new FormBody.Builder()
                .add("client_id", CLIENT_ID)
                .add("client_secret", CLIENT_SECRET)
                .add("grant_type", "refresh_token")
                .add("refresh_token", refreshToken)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful() ? gson.fromJson(response.body().string(), Map.class) : null;
        }
    }

    public static void main(String[] args) throws IOException {

        try {
            Map<String, String> res = refreshStravaToken("d35c12def42b1317940ceed32a0586e078f92dd8");

            // In ra kết quả
            System.out.println("Polyline: " + res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
