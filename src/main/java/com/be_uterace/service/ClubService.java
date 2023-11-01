package com.be_uterace.service;

import com.be_uterace.payload.request.ClubAddDto;
import com.be_uterace.payload.request.ClubUpdateDto;
import com.be_uterace.payload.request.UserClubRequest;
import com.be_uterace.payload.response.ClubDetailResponse;
import com.be_uterace.payload.response.ClubPaginationResponse;
import com.be_uterace.payload.response.ResponseObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

public interface ClubService {
    ClubPaginationResponse getAllClub(int current_page, int per_page);

    ClubDetailResponse getClubDetail(int club_id);

    ResponseEntity<ResponseObject> createClub(ClubAddDto clubAddDto, Authentication authentication);

    ResponseEntity<ResponseObject> updateClub(ClubUpdateDto clubUpdateDto, Authentication authentication);

    ResponseEntity<ResponseObject> deleteClub(Integer club_id, Authentication authentication);

    ResponseEntity<ResponseObject> deleteMember(UserClubRequest userClubRequest);

    ResponseEntity<ResponseObject> changeAdmin(UserClubRequest userClubRequest);

    ClubPaginationResponse getOwnClubCreated(int current_page, int per_page, Authentication authentication);

    ClubPaginationResponse getClubJoined(int current_page, int per_page, Authentication authentication);

    ResponseObject joinClub(int club_id, Authentication auth);

    ResponseObject leaveClub(int club_id, Authentication auth);

}
