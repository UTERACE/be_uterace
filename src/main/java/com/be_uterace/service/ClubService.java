package com.be_uterace.service;

import com.be_uterace.entity.User;
import com.be_uterace.payload.request.*;
import com.be_uterace.payload.response.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface ClubService {
    ClubPaginationResponse getAllClub(int current_page, int per_page, String search);

    ClubDetailResponse getClubDetail(int club_id);

    ResponseObject createClub(ClubAddDto clubAddDto, Authentication authentication);

    ResponseEntity<ResponseObject> updateClub(ClubUpdateDto clubUpdateDto, Authentication authentication);

    ResponseEntity<ResponseObject> deleteClub(Integer club_id, Authentication authentication);

    ResponseEntity<ResponseObject> deleteMember(UserClubRequest userClubRequest);

    ResponseEntity<ResponseObject> changeAdmin(UserClubRequest userClubRequest);

    List<UserFindResponse> findUserByClubId(int club_id, String search);

    ClubPaginationResponse getOwnClubCreated(int current_page, int per_page, String search, Authentication authentication);

    ClubPaginationResponse getManageClubCreated(int current_page, int per_page, String search, Authentication authentication);

    ClubPaginationResponse getClubJoined(int current_page, int per_page, String search, Authentication authentication);

    ClubPaginationResponse getClubCreatedByUser(Long user_id, int current_page, int per_page, String search, Authentication authentication);

    ResponseObject joinClub(int club_id, Authentication auth);

    ResponseObject leaveClub(int club_id, Authentication auth);

    Boolean checkJoinClub(int club_id, Authentication auth);

    ResponseObject deleteActivity(DeleteActivityClub req);

    RecentActiveResponse getRecentActivity(int current_page, int per_page, Integer clubId, String search, int hours);

    RankingMemberResponse getScoreBoardClubMember(int club_id, int current_page, int per_page, String search_name);

}
