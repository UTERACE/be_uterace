package com.be_uterace.service;

import com.be_uterace.entity.User;
import com.be_uterace.payload.request.ChangePasswordDto;
import com.be_uterace.payload.request.UpdateDto;
import com.be_uterace.payload.response.*;
import org.springframework.security.core.Authentication;

public interface UserService {

    User getCurrentLogin();
    UserResponse getUserInfo(Authentication authentication);

    ResponseObject changePassword(ChangePasswordDto changePasswordDto, Authentication authentication);

    UserUpdateResponse updateUser(UpdateDto updateDto, Authentication authentication);

    RecentActiveResponse getRecentActivity(int current_page, int per_page, Long userId, String search, int hour);

    RecentActiveResponse getRecentActivity(int current_page, int per_page, String search, int hour, Authentication authentication);

    UserStatisticResponse getSummaryActivity(Long user_id);
}
