package com.be_uterace.service;

import com.be_uterace.payload.response.ResponseObject;

public interface ManagePostService {
    ResponseObject lockPost(Integer post_id);

    ResponseObject unlockPost(Integer post_id);

    ResponseObject outstandingPost(Integer post_id);

    ResponseObject notOutstandingPost(Integer post_id);

}
