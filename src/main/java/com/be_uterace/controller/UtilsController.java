package com.be_uterace.controller;

import com.be_uterace.entity.Run;
import com.be_uterace.entity.User;
import com.be_uterace.repository.RunRepository;
import com.be_uterace.repository.UserRepository;
import com.be_uterace.utils.PolylineDecoder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Map;

import static com.be_uterace.utils.PolylineDecoder.decode;
import static com.be_uterace.utils.StravaUtils.getActivityInfoById;
import static com.be_uterace.utils.StravaUtils.refreshStravaToken;

import org.json.JSONObject;

@RestController
@RequestMapping("/api")
public class UtilsController {
    private RunRepository runRepository;

    private UserRepository userRepository;

    private final ObjectMapper objectMapper;

    public UtilsController(RunRepository runRepository, UserRepository userRepository, ObjectMapper objectMapper) {
        this.runRepository = runRepository;
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/decode_polyline/{activityId}")
    @Transactional
    public ResponseEntity<Object> decodePolyline(@PathVariable Long activityId) throws IOException {
        Run run = runRepository.findById(activityId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Activity not found"));

        if (run.getSummaryPolyline() == null || run.getSummaryPolyline().isEmpty()) {
            // Handle case when the polyline is empty
            User user = run.getUser();
            String activityInfo = getActivityInfoById(run.getStravaRunId(), user.getStravaAccessToken());
            if(activityInfo==null){
                Map<String, String> res = refreshStravaToken(user.getStravaRefreshToken());
                user.setStravaAccessToken(res.get("access_token"));
                user.setStravaRefreshToken(res.get("refresh_token"));
                activityInfo = getActivityInfoById(run.getStravaRunId(), res.get("access_token"));
                userRepository.save(user);
            }
            JSONObject jsonObject = new JSONObject(activityInfo);
            JSONObject mapObject = jsonObject.getJSONObject("map");
            String polyline = mapObject.getString("polyline");

            if (polyline == null || polyline.isEmpty())
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Empty polyline");
            run.setSummaryPolyline(polyline);
            runRepository.save(run);
            String jsonCoordinates = PolylineDecoder.decode(polyline);
            Object jsonObject1 = objectMapper.readValue(jsonCoordinates, Object.class);
            return new ResponseEntity<>(jsonObject1, HttpStatus.OK);
        }

        String expression = run.getSummaryPolyline();
        String jsonCoordinates = PolylineDecoder.decode(expression);

        try {
            Object jsonObject = objectMapper.readValue(jsonCoordinates, Object.class);
            return new ResponseEntity<>(jsonObject, HttpStatus.OK);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error processing JSON", e);
        }
    }
}
