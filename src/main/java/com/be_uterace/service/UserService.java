package com.be_uterace.service;

import com.be_uterace.entity.User;
import com.be_uterace.payload.response.UserResponse;
import org.springframework.security.core.Authentication;

public interface UserService {
    UserResponse getUserInfo(Authentication authentication);
}
