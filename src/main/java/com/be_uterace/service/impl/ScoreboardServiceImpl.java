package com.be_uterace.service.impl;

import com.be_uterace.payload.response.ClubRankingResponse;
import com.be_uterace.payload.response.ScoreboardResponse;
import com.be_uterace.payload.response.UserRankingResponse;
import com.be_uterace.projection.ClubRankingProjection;
import com.be_uterace.projection.UserRankingProjection;
import com.be_uterace.repository.ClubRepository;
import com.be_uterace.repository.UserRepository;
import com.be_uterace.service.ScoreboardService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ScoreboardServiceImpl implements ScoreboardService {

    private ClubRepository clubRepository;

    private UserRepository userRepository;

    public ScoreboardServiceImpl(ClubRepository clubRepository, UserRepository userRepository) {
        this.clubRepository = clubRepository;
        this.userRepository = userRepository;
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
                .totalClub((int) scoreboardClubProjections.getTotalElements())
                .currentPage(scoreboardClubProjections.getNumber())
                .totalPage(scoreboardClubProjections.getTotalPages())
                .ranking_club(rankingClubList)
                .build();
    }

    @Override
    public ScoreboardResponse getScoreboardUser(int month, int year, int current_page, int per_page) {
        Pageable pageable = PageRequest.of(current_page, per_page);
        Page<UserRankingProjection> userRankingResponsePage = userRepository.findScoreboardUser(month,year,pageable);
        List<UserRankingProjection> userRankingResponseList = userRankingResponsePage.getContent();

        List<UserRankingResponse> rankingUserList = new ArrayList<>();
        for (UserRankingProjection userRankingResponse : userRankingResponseList){
            UserRankingResponse userRanking = new UserRankingResponse();
            userRanking.setUser_id(userRankingResponse.getUserId());
            userRanking.setRanking(userRankingResponse.getRanking());
            userRanking.setFirst_name(userRankingResponse.getFirstName());
            userRanking.setLast_name(userRankingResponse.getLastName());
            userRanking.setGender(userRankingResponse.getGender());
            userRanking.setPace(userRankingResponse.getPace());
            userRanking.setOrganization(userRankingResponse.getOrganization());
            userRanking.setImage(userRankingResponse.getAvatarPath());
            userRanking.setTotal_distance(userRankingResponse.getTotalDistance());
            rankingUserList.add(userRanking);
        }
        return ScoreboardResponse.builder()
                .perPage(userRankingResponsePage.getSize())
                .totalUser((int) userRankingResponsePage.getTotalElements())
                .currentPage(userRankingResponsePage.getNumber())
                .totalPage(userRankingResponsePage.getTotalPages())
                .ranking_user(rankingUserList)
                .build();
    }
}
