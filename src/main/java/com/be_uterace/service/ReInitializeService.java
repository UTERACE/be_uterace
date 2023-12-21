package com.be_uterace.service;

import com.be_uterace.payload.response.ResponseObject;

import java.io.IOException;

public interface ReInitializeService {
    ResponseObject reInitializeAll() throws IOException, InterruptedException;

    ResponseObject reInitializeForUser(Long user_id) throws InterruptedException;
}
