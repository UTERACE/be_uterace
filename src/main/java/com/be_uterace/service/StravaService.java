package com.be_uterace.service;

import com.be_uterace.payload.response.ConnectStravaResponse;

import java.io.IOException;

public interface StravaService {
    ConnectStravaResponse connectStrava(String code) throws IOException;
    ConnectStravaResponse disconnectStrava();
    ConnectStravaResponse statusStrava();
}
