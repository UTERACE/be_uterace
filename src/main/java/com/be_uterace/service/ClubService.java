package com.be_uterace.service;

import com.be_uterace.payload.request.*;
import com.be_uterace.payload.response.ClubDetailResponse;
import com.be_uterace.payload.response.ClubPaginationResponse;
import com.be_uterace.payload.response.RecentActiveResponse;
import com.be_uterace.payload.response.ResponseObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

public interface ClubService {
    ClubPaginationResponse getAllClub(int current_page, int per_page, String search);

    ClubDetailResponse getClubDetail(int club_id);

    ResponseEntity<ResponseObject> createClub(ClubAddDto clubAddDto, Authentication authentication);

    ResponseEntity<ResponseObject> updateClub(ClubUpdateDto clubUpdateDto, Authentication authentication);

    ResponseEntity<ResponseObject> deleteClub(Integer club_id, Authentication authentication);

    ResponseEntity<ResponseObject> deleteMember(UserClubRequest userClubRequest);

    ResponseEntity<ResponseObject> changeAdmin(UserClubRequest userClubRequest);

    ClubPaginationResponse getOwnClubCreated(int current_page, int per_page, String search, Authentication authentication);

    ClubPaginationResponse getClubJoined(int current_page, int per_page, String search, Authentication authentication);

    ResponseObject joinClub(int club_id, Authentication auth);

    ResponseObject leaveClub(int club_id, Authentication auth);

    Boolean checkJoinClub(int club_id, Authentication auth);

    ResponseObject deleteActivity(DeleteActivityClub req);

    RecentActiveResponse getRecentActivity(int current_page, int per_page, Long userId, String search, int hours);

}
