package com.be_uterace.service;

import com.be_uterace.payload.response.ResponseObject;

public interface ManagePostService {
    ResponseObject blockPost(Integer post_id);
}
