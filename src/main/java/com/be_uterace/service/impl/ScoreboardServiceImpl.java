package com.be_uterace.service.impl;

import com.be_uterace.payload.response.ClubRankingResponse;
import com.be_uterace.payload.response.ResponseObject;
import com.be_uterace.payload.response.ScoreboardResponse;
import com.be_uterace.projection.ClubRankingProjection;
import com.be_uterace.projection.ScoreboardClubProjection;
import com.be_uterace.repository.ClubRepository;
import com.be_uterace.service.ScoreboardService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ScoreboardServiceImpl implements ScoreboardService {

    private ClubRepository clubRepository;

    public ScoreboardServiceImpl(ClubRepository clubRepository) {
        this.clubRepository = clubRepository;
    }

    @Override
    public ScoreboardResponse getScoreboardClub(int month, int year, int current_page, int per_page) {
        Pageable pageable = PageRequest.of(current_page, per_page);
        Page<ClubRankingProjection> scoreboardClubProjections = clubRepository.findScoreboardClub(month, year, pageable);

        List<ClubRankingProjection> content = scoreboardClubProjections.getContent();

        List<ClubRankingResponse> rankingClubList = new ArrayList<>();

        for (ClubRankingProjection club : content) {
            ClubRankingResponse rankingClubItem = new ClubRankingResponse();
            rankingClubItem.setClub_id(club.getClubId());
            rankingClubItem.setRanking(club.getClubRanking());
            rankingClubItem.setName(club.getClubName());
            rankingClubItem.setImage(club.getPicturePath());
            rankingClubItem.setTotal_distance(club.getClubTotalDistance());
            rankingClubItem.setTotal_members(club.getMemberCount());
            rankingClubItem.setTotal_activities(club.getTotalActivities());
            rankingClubList.add(rankingClubItem);
        }

        return ScoreboardResponse.builder()
                .perPage(scoreboardClubProjections.getSize())
                .totalClub(content.size())
                .currentPage(scoreboardClubProjections.getNumber())
                .totalPage(scoreboardClubProjections.getTotalPages())
                .ranking_club(rankingClubList)
                .build();
    }
}
