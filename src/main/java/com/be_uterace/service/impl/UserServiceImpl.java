package com.be_uterace.service.impl;

import com.be_uterace.entity.Post;
import com.be_uterace.entity.Run;
import com.be_uterace.entity.User;
import com.be_uterace.payload.request.ChangePasswordDto;
import com.be_uterace.payload.request.UpdateDto;
import com.be_uterace.payload.response.*;
import com.be_uterace.repository.*;
import com.be_uterace.service.FileService;
import com.be_uterace.service.UserService;
import com.be_uterace.utils.Constant;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.be_uterace.mapper.UserMapper.convertFromUserToUserResponse;
import static com.be_uterace.utils.DateConverter.convertStringToDate;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private RunRepository runRepository;
    private PasswordEncoder passwordEncoder;
    private AreaRepository areaRepository;
    private UserClubRepository userClubRepository;
    private UserEventRepository userEventRepository;

    private ModelMapper modelMapper;
    private FileService fileService;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
                           AreaRepository areaRepository,
                           ModelMapper modelMapper,
                           FileService fileService,
                           RunRepository runRepository,
                           UserClubRepository userClubRepository,
                           UserEventRepository userEventRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.areaRepository = areaRepository;
        this.modelMapper = modelMapper;
        this.fileService = fileService;
        this.runRepository = runRepository;
        this.userClubRepository = userClubRepository;
        this.userEventRepository = userEventRepository;

    }

    @Override
    public UserResponse getUserInfo(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            String username = userDetails.getUsername();
            Optional<User> user = userRepository.findByUsername(username);
            User user1 = user.get();
            return convertFromUserToUserResponse(user1);
        }
        return null;
    }

    @Override
    public ResponseObject changePassword(ChangePasswordDto changePasswordDto, Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            String username = userDetails.getUsername();
            Optional<User> userOptional = userRepository.findByUsername(username);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                if (passwordEncoder.matches(changePasswordDto.getOld_password(), user.getPassword())) {
                    user.setPassword(passwordEncoder.encode(changePasswordDto.getNew_password()));
                    userRepository.save(user);
                    return ResponseObject.builder()
                            .status(200)
                            .message("Update password successfully!")
                            .build();
                }
            }
        }
        return null;
    }

    @Override
    public ResponseObject updateUser(UpdateDto updateDto, Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            String username = userDetails.getUsername();
            Optional<User> userOptional = userRepository.findByUsername(username);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                user.setFirstName(!Objects.equals(updateDto.getFirstname(), "") ? updateDto.getFirstname() : user.getFirstName());
                user.setLastName(!Objects.equals(updateDto.getLastname(), "") ? updateDto.getLastname() : user.getLastName());
                user.setEmail(!Objects.equals(updateDto.getEmail(), "") ? updateDto.getEmail() : user.getEmail());
                user.setTelNum(!Objects.equals(updateDto.getTelNumber(), "") ? updateDto.getTelNumber() : user.getTelNum());
                user.setDateOfBirth(!Objects.equals(updateDto.getBirthday(), "") ? convertStringToDate(updateDto.getBirthday()) : user.getDateOfBirth());
                user.setGender(!Objects.equals(updateDto.getGender(), "") ? updateDto.getGender() : user.getGender());
                user.setHomeNumber(!Objects.equals(updateDto.getAddress(), "") ? updateDto.getAddress() : user.getHomeNumber());
                user.setArea(!Objects.equals(updateDto.getProvince(), "") && !Objects.equals(updateDto.getDistrict(), "") && !Objects.equals(updateDto.getWard(), "") ? areaRepository.findArea(updateDto.getProvince(),
                        updateDto.getDistrict(),
                        updateDto.getWard()) : user.getArea());
//                user.setArea(areaRepository.findArea(updateDto.getProvince(),
//                        updateDto.getDistrict(),
//                        updateDto.getWard()));
                if (!user.getAvatarPath().equals(updateDto.getImage()) && !Objects.equals(updateDto.getImage(), "")){
                    if (Objects.equals(user.getAvatarPath(), "")){
                        user.setAvatarPath(fileService.saveImage(updateDto.getImage()));
                    }else if (fileService.deleteImage(user.getAvatarPath())){
                        System.out.println("Delete image success");
                        user.setAvatarPath(fileService.saveImage(updateDto.getImage()));
                    }
                }

                userRepository.save(user);
                return ResponseObject.builder()
                        .status(200)
                        .message("Cập nhật thông tin ngừoi dùng \n" +
                                " thành công!")
                        .build();
            }
        }
        return ResponseObject.builder()
                .status(400)
                .message("Cập nhật thông tin ngừoi dùng \n" +
                        " thất bại!")
                .build();
    }

    @Override
    public RecentActiveResponse getRecentActivity(int current_page, int per_page,Long userId, String search, int hours) {
        Page<Run> runPage;
        Pageable pageable = PageRequest.of(current_page - 1, per_page);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime thresholdDateTime = now.minusHours(hours);
        Timestamp thresholdTimestamp = Timestamp.valueOf(thresholdDateTime);

        if(search==null || search.equals("")) {
            runPage = runRepository.findRunsByDateTimeAndName(thresholdTimestamp, null, userId,pageable);
        } else {
            runPage = runRepository.findRunsByDateTimeAndName(thresholdTimestamp, search,userId,pageable);
        }
        List<Run> runList = runPage.getContent();
        List<RecentActiveResponse.Activity> arrayList = new ArrayList<>();
        for (Run run : runList){
            RecentActiveResponse.Activity activityResponse = new RecentActiveResponse.Activity();
            activityResponse.setActivity_id(run.getRunId());
            activityResponse.setMember_id(run.getUser().getUserId());
            activityResponse.setMember_image(run.getUser().getAvatarPath());
            activityResponse.setMember_name(run.getUser().getFirstName()+" "+run.getUser().getLastName());
            activityResponse.setActivity_start_date(run.getCreatedAt());
            activityResponse.setActivity_distance(run.getDistance());
            activityResponse.setActivity_pace(run.getPace());
            activityResponse.setActivity_duration(run.getDuration());
            activityResponse.setActivity_name(run.getName());
            activityResponse.setActivity_type(run.getType());
            activityResponse.setActivity_link_strava("https://www.strava.com/activities/"+run.getStravaRunId());
            activityResponse.setActivity_map(run.getSummaryPolyline());
            activityResponse.setStatus(run.getStatus());
            activityResponse.setReason(run.getReason());
            arrayList.add(activityResponse);
        }
        return RecentActiveResponse.builder()
                .per_page(runPage.getSize())
                .total_activities((int) runPage.getTotalElements())
                .current_page(runPage.getNumber() + 1)
                .total_page(runPage.getTotalPages())
                .activities(arrayList)
                .build();
    }

    @Override
    public RecentActiveResponse getRecentActivity(int current_page, int per_page, String search, int hour, Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            String username = userDetails.getUsername();
            Optional<User> userOptional = userRepository.findByUsername(username);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                return getRecentActivity(current_page,per_page,user.getUserId(),search,hour);
            }
        }
        return null;
    }

    @Override
    public UserStatisticResponse getSummaryActivity(Long user_id) {
        if (user_id == null){
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = userRepository.findByUsername(authentication.getName()).orElse(null);
            user_id = user.getUserId();
        }
        List<Object[]> rawStats = runRepository.getUserRunStats(user_id);
        Optional<User> userOptional = userRepository.findById(user_id);
        User user = userOptional.get();
        List<UserStatisticResponse.ChartDate> statisticDateResponses = new ArrayList<>();
        List<UserStatisticResponse.ChartMonth> statisticMonthResponses = new ArrayList<>();

        int rowIndex = 0;
        for (Object[] row : rawStats) {
            String formattedDate = (String) row[0];
            Double totalDistance = (Double) row[1];
            Double averagePace = (Double) row[2];

            if (rowIndex < 7) {
                // Add to statisticDateResponses for the first 7 rows
                statisticDateResponses.add(new UserStatisticResponse.ChartDate(formattedDate, totalDistance, averagePace));
            } else {
                // Add to statisticMonthResponses for the last 2 rows
                statisticMonthResponses.add(new UserStatisticResponse.ChartMonth(formattedDate, totalDistance, averagePace));
            }

            rowIndex++;
        }
        return UserStatisticResponse.builder()
                .user_id(user.getUserId())
                .image(user.getAvatarPath())
                .total_distance(user.getTotalDistance())
                .chart_date(statisticDateResponses)
                .chart_month(statisticMonthResponses)
                .ranking(user.getRanking())
                .avg_pace(user.getPace())
                .first_name(user.getFirstName())
                .last_name(user.getLastName())
                .strava_user_link((user.getStravaId() != null) ? "https://www.strava.com/athletes/" + user.getStravaId().toString() : null)
                .total_clubs(userClubRepository.countClubsByUserId(user_id))
                .total_activities(runRepository.countRunsByUserId(user_id))
                .total_event(userEventRepository.countEventsByUserId(user_id))
                .build();
    }
}
