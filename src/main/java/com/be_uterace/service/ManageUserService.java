package com.be_uterace.service;

import com.be_uterace.payload.response.ManageUserInitializeResponse;
import com.be_uterace.payload.response.ManageUserStatusResponse;
import com.be_uterace.payload.response.ResponseObject;

public interface ManageUserService {

    ResponseObject lockUser(Integer user_id);

    ResponseObject unlockUser(Integer user_id);

    ManageUserStatusResponse findAllUserStatus(int current_page, int per_page, String search);

    ManageUserInitializeResponse findAllUserInitialize(int current_page, int per_page, String search);
}
