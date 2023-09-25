package com.be_uterace.service;

import com.be_uterace.payload.request.LoginDto;
import com.be_uterace.payload.request.RegisterDto;
import com.be_uterace.payload.request.ResetPasswordDto;
import com.be_uterace.payload.request.ThirdPartyDto;
import com.be_uterace.payload.response.LoginResponse;
import com.be_uterace.payload.response.RefreshTokenResponse;
import com.be_uterace.payload.response.ResponseObject;
import com.be_uterace.payload.response.ThirdPartyResponse;

public interface AuthService {
    LoginResponse login(LoginDto loginDto);

    ThirdPartyResponse thirdParty(ThirdPartyDto thirdPartyDto);

    ResponseObject register(RegisterDto registerDto);

    RefreshTokenResponse refreshToken(String refreshToken);

    ResponseObject resetPassword(ResetPasswordDto registerDto);

}
