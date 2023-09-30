package com.be_uterace.service;

import com.be_uterace.entity.User;
import com.be_uterace.payload.request.ChangePasswordDto;
import com.be_uterace.payload.request.UpdateDto;
import com.be_uterace.payload.response.ResponseObject;
import com.be_uterace.payload.response.UserResponse;
import org.springframework.security.core.Authentication;

public interface UserService {
    UserResponse getUserInfo(Authentication authentication);

    ResponseObject changePassword(ChangePasswordDto changePasswordDto, Authentication authentication);

    ResponseObject updateUser(UpdateDto updateDto, Authentication authentication);


}
