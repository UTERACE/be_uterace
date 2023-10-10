package com.be_uterace.service;

import com.be_uterace.payload.response.ClubPaginationResponse;
import org.springframework.data.domain.Pageable;

public interface ClubService {
    ClubPaginationResponse getAllClub(int current_page, int per_page);
}
