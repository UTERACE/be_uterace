package com.be_uterace.service;

import com.be_uterace.payload.request.LoginDto;
import com.be_uterace.payload.request.RegisterDto;
import com.be_uterace.payload.response.LoginResponse;
import com.be_uterace.payload.response.ResponseObject;

public interface AuthService {
    LoginResponse login(LoginDto loginDto);

    ResponseObject register(RegisterDto registerDto);

}
