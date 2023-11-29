package com.be_uterace.service.impl;

import com.be_uterace.entity.Run;
import com.be_uterace.entity.User;
import com.be_uterace.payload.response.ResponseObject;
import com.be_uterace.payload.response.stravaresponse.WebhookResponse;
import com.be_uterace.repository.RunRepository;
import com.be_uterace.repository.UserRepository;
import com.be_uterace.service.WebhookService;
import com.be_uterace.utils.StatusCode;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.be_uterace.utils.Format.formatSeconds;
import static com.be_uterace.utils.StravaUtils.getActivityInfoById;
import static com.be_uterace.utils.StravaUtils.refreshStravaToken;
import static com.be_uterace.utils.Validation.checkPace;

@Service
public class WebhookServiceImpl implements WebhookService {
    private UserRepository userRepository;
    private RunRepository runRepository;

    public WebhookServiceImpl(UserRepository userRepository, RunRepository runRepository) {
        this.userRepository = userRepository;
        this.runRepository = runRepository;
    }

    @Override
    public ResponseObject updateRunEventWebhook(WebhookResponse request) {
        Optional<User> userOptional = Optional.ofNullable(userRepository.findUserByStravaId(request.getOwner_id()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Strava account not exist in database"));

        Optional<Run> runOptional = Optional.ofNullable(runRepository.findRunByStravaRunId(request.getObject_id())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Activity Not Found")));

        Run run = runOptional.get();

        if (request.getUpdates().containsKey("title")) {
            run.setName((String) request.getUpdates().get("title"));
        }

        if (request.getUpdates().containsKey("type")) {
            run.setType((String) request.getUpdates().get("type"));
        }
        runRepository.save(run);
        return new ResponseObject(StatusCode.SUCCESS,"SUCCESS");

    }

    @Override
    public ResponseObject addRunEventWebhook(WebhookResponse request) throws IOException, ParseException {
        Optional<User> userOptional = Optional.ofNullable(userRepository.findUserByStravaId(request.getOwner_id())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Strava account not exist in database")));
        boolean runCheck = runRepository.existsByStravaRunId(request.getObject_id());
        if(runCheck){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Activity Exist");
        }
        User user = userOptional.get();
        String activityInfo = getActivityInfoById(request.getObject_id(), user.getStravaAccessToken());
        if(activityInfo==null){
            Map<String, String> res = refreshStravaToken(user.getStravaRefreshToken());
            user.setStravaAccessToken(res.get("access_token"));
            user.setStravaRefreshToken(res.get("refresh_token"));
            activityInfo = getActivityInfoById(request.getObject_id(), res.get("access_token"));
            userRepository.save(user);
        }

        JSONObject jsonObject = new JSONObject(activityInfo);
        Double averageSpeed = jsonObject.getDouble("average_speed");
        double pace = (averageSpeed != null && averageSpeed != 0) ? 60 / (averageSpeed * 3.6) : 0;
        String dateString =  jsonObject.getString("start_date_local");
        System.out.println(dateString);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

        Date start_date_local = dateFormat.parse(dateString);
        Run run = new Run();
        run.setUser(user);
        run.setStravaRunId(jsonObject.getLong("id"));
        run.setName(jsonObject.getString("name"));
        run.setDistance(jsonObject.getDouble("distance")/1000);
        run.setType(jsonObject.getString("type"));
        run.setDuration(formatSeconds(jsonObject.getInt("elapsed_time")));
        run.setPace(pace);
        run.setStatus(checkPace(pace));
        run.setStepRate(getNullableDouble(jsonObject, "step_rate"));
        run.setHeartRate(getNullableDouble(jsonObject, "average_heartrate"));
        run.setCalori(getNullableDouble(jsonObject, "calories"));


        run.setCreatedAt(start_date_local);

        JSONObject mapObject = jsonObject.getJSONObject("map");
        String polyline = mapObject.getString("polyline");
        run.setSummaryPolyline(polyline != null ? polyline : null);

        runRepository.save(run);
        return new ResponseObject(StatusCode.SUCCESS,"SUCCESS");

    }

    public static Double getNullableDouble(JSONObject jsonObject, String key) {
        return jsonObject.isNull(key) ? null : jsonObject.getDouble(key);
    }
}
