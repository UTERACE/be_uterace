package com.be_uterace.service.impl;

import com.be_uterace.entity.Organization;
import com.be_uterace.payload.response.ScoreboardClubResponse;
import com.be_uterace.payload.response.ScoreboardUserResponse;
import com.be_uterace.projection.ClubRankingProjection;
import com.be_uterace.projection.UserRankingProjection;
import com.be_uterace.repository.ClubRepository;
import com.be_uterace.repository.OrganizationRepository;
import com.be_uterace.repository.UserEventRepository;
import com.be_uterace.repository.UserRepository;
import com.be_uterace.service.ScoreboardService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ScoreboardServiceImpl implements ScoreboardService {

    private ClubRepository clubRepository;
    private OrganizationRepository organizationRepository;
    private UserEventRepository userEventRepository;
    private UserRepository userRepository;

    public ScoreboardServiceImpl(ClubRepository clubRepository, UserRepository userRepository,
                                 OrganizationRepository organizationRepository, UserEventRepository userEventRepository) {
        this.clubRepository = clubRepository;
        this.userRepository = userRepository;
        this.organizationRepository = organizationRepository;
        this.userEventRepository = userEventRepository;
    }

    @Override
    public ScoreboardClubResponse getScoreboardClub(int month, int year, int current_page, int per_page, String search_name) {
        Pageable pageable = PageRequest.of(current_page-1, per_page);
        Page<ClubRankingProjection> scoreboardClubProjections = clubRepository.findScoreboardClub(month, year, search_name,pageable);

        List<ClubRankingProjection> content = scoreboardClubProjections.getContent();

        List<ScoreboardClubResponse.RankedClub> rankingClubList = new ArrayList<>();

        for (ClubRankingProjection club : content) {
            ScoreboardClubResponse.RankedClub rankingClubItem = new ScoreboardClubResponse.RankedClub();
            rankingClubItem.setClub_id(club.getC_club_id());
            rankingClubItem.setRanking(club.getC_ranking());
            rankingClubItem.setName(club.getC_club_name());
            rankingClubItem.setImage(club.getC_picture_path());
            rankingClubItem.setTotal_distance(club.getC_total_distance());
            rankingClubItem.setTotal_members(club.getC_num_of_attendee());
            rankingClubItem.setTotal_activities(club.getC_total_activities());
            rankingClubList.add(rankingClubItem);
        }

        return ScoreboardClubResponse.builder()
                .per_page(scoreboardClubProjections.getSize())
                .total_club((int) scoreboardClubProjections.getTotalElements())
                .current_page(scoreboardClubProjections.getNumber()+1)
                .total_page(scoreboardClubProjections.getTotalPages())
                .ranking_club(rankingClubList)
                .build();
    }

    @Override
    public ScoreboardUserResponse getScoreboardUser(int month, int year, int current_page, int per_page, String search_name) {
        if(Objects.equals(search_name, "")){
            search_name=null;
        }
        if (month == 0){
            year = 0;
        }
        Pageable pageable = PageRequest.of(current_page-1, per_page);
        Page<UserRankingProjection> userRankingResponsePage = userRepository.findScoreboardUser(month,year,search_name,pageable);
        List<UserRankingProjection> userRankingResponseList = userRankingResponsePage.getContent();

        List<ScoreboardUserResponse.UserRanking> rankingUserList = new ArrayList<>();
        for (UserRankingProjection userRankingResponse : userRankingResponseList){
            ScoreboardUserResponse.UserRanking userRanking = new ScoreboardUserResponse.UserRanking();
            userRanking.setUser_id(userRankingResponse.getU_user_id());
            userRanking.setRanking(userRankingResponse.getU_ranking());
            userRanking.setFirst_name(userRankingResponse.getU_firstname());
            userRanking.setLast_name(userRankingResponse.getU_lastname());
            userRanking.setGender(userRankingResponse.getU_gender());
            userRanking.setPace(userRankingResponse.getU_pace());
            Long orgId = userRankingResponse.getU_org_id();
            userRanking.setOrganization(
                    Optional.ofNullable(orgId)
                            .map(id -> organizationRepository.findById(id).map(Organization::getOrgName).orElse(null))
                            .orElse(null)
            );
            userRanking.setImage(userRankingResponse.getU_avatar_path());
            userRanking.setTotal_distance(userRankingResponse.getU_total_distance());
            rankingUserList.add(userRanking);
        }
        return ScoreboardUserResponse.builder()
                .per_page(userRankingResponsePage.getSize())
                .total_user((int) userRankingResponsePage.getTotalElements())
                .current_page(userRankingResponsePage.getNumber()+1)
                .total_page(userRankingResponsePage.getTotalPages())
                .ranking_user(rankingUserList)
                .build();
    }
}
