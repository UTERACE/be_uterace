package com.be_uterace.service;

import com.be_uterace.entity.User;
import com.be_uterace.payload.request.ChangePasswordDto;
import com.be_uterace.payload.request.UpdateDto;
import com.be_uterace.payload.response.RecentActiveResponse;
import com.be_uterace.payload.response.ResponseObject;
import com.be_uterace.payload.response.UserResponse;
import com.be_uterace.payload.response.UserStatisticResponse;
import org.springframework.security.core.Authentication;

public interface UserService {
    UserResponse getUserInfo(Authentication authentication);

    ResponseObject changePassword(ChangePasswordDto changePasswordDto, Authentication authentication);

    ResponseObject updateUser(UpdateDto updateDto, Authentication authentication);

    RecentActiveResponse getRecentActivity(int current_page, int per_page, Long userId, String search, int hour);

    UserStatisticResponse getSummaryActivity(Long user_id);
}
