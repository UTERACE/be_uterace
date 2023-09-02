package com.be_uterace.service;

import com.be_uterace.payload.request.LoginDto;

public interface AuthService {
    String login(LoginDto loginDto);
}
