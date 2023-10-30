package com.be_uterace.service;

import com.be_uterace.payload.request.DeleteActivityEvent;
import com.be_uterace.payload.response.ResponseObject;

public interface UEActivityService {
    ResponseObject deleteActivity(DeleteActivityEvent req);
}
