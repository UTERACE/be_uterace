package com.be_uterace.service;

import com.be_uterace.payload.response.ManageClubSearchResponse;
import com.be_uterace.payload.response.ResponseObject;

public interface ManageClubService {
    ResponseObject lockClub(Integer club_id);

    ResponseObject unlockClub(Integer club_id);

    ResponseObject outstandingClub(Integer club_id);

    ResponseObject notOutstandingClub(Integer club_id);

//    ManageClubSearchResponse searchCLub(int current_page, int per_page, String search);


}
