package com.be_uterace.service;

import com.be_uterace.payload.request.LockRequest;
import com.be_uterace.payload.response.ManageClubSearchResponse;
import com.be_uterace.payload.response.ManagePostSearchResponse;
import com.be_uterace.payload.response.ResponseObject;

public interface ManagePostService {
    ResponseObject lockPost(Integer post_id, LockRequest lockRequest);

    ResponseObject unlockPost(Integer post_id);

    ResponseObject outstandingPost(Integer post_id);

    ResponseObject notOutstandingPost(Integer post_id);

    ManagePostSearchResponse searchPost(int current_page, int per_page, String search);


}
