package com.be_uterace.service;

import com.be_uterace.payload.response.ConnectStravaResponse;

public interface StravaService {
    ConnectStravaResponse connectStrava(String code);
}
