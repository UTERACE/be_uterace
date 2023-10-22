package com.be_uterace.service;

import com.be_uterace.payload.request.ClubAddDto;
import com.be_uterace.payload.request.ClubUpdateDto;
import com.be_uterace.payload.response.ClubDetailResponse;
import com.be_uterace.payload.response.ClubPaginationResponse;
import com.be_uterace.payload.response.ResponseObject;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

public interface ClubService {
    ClubPaginationResponse getAllClub(int current_page, int per_page);

    ClubDetailResponse getClubDetail(int club_id);

    ResponseObject createClub(ClubAddDto clubAddDto, Authentication authentication);

    ResponseObject updateClub(ClubUpdateDto clubUpdateDto, Authentication authentication);

    ResponseObject deleteClub(Integer club_id, Authentication authentication);
}
