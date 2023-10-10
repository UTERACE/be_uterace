package com.be_uterace.service.impl;

import com.be_uterace.payload.response.ClubPaginationResponse;
import com.be_uterace.payload.response.ClubResponse;
import com.be_uterace.projection.ClubProjection;
import com.be_uterace.projection.UserRankingProjection;
import com.be_uterace.repository.ClubRepository;
import com.be_uterace.service.ClubService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClubServiceImpl implements ClubService {

    private ClubRepository clubRepository;

    public ClubServiceImpl(ClubRepository clubRepository) {
        this.clubRepository = clubRepository;
    }

    @Override
    public ClubPaginationResponse getAllClub(int current_page, int per_page) {
        Pageable pageable = PageRequest.of(current_page, per_page);
        Page<ClubProjection> clubProjectionPage = clubRepository.findAllClubPagination(pageable);
        List<ClubProjection> clubProjectionList = clubProjectionPage.getContent();
        List<ClubResponse> clubResponseList = new ArrayList<>();
        for (ClubProjection item : clubProjectionList){
            ClubResponse clubResponse = new ClubResponse();
            clubResponse.setClub_id(item.getClubId());
            clubResponse.setName(item.getClubName());
            clubResponse.setImage(item.getPicturePath());
            clubResponse.setTotal_member(item.getMemberCount());
            clubResponse.setTotal_distance(item.getClubTotalDistance());
            clubResponseList.add(clubResponse);
        }

        return ClubPaginationResponse.builder()
                .per_page(clubProjectionPage.getSize())
                .current_page(clubProjectionPage.getNumber())
                .totalPage(clubProjectionPage.getTotalPages())
                .total_clubs((int) clubProjectionPage.getTotalElements())
                .clubs(clubResponseList).build();
    }
}
