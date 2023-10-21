package com.be_uterace.service.impl;

import com.be_uterace.entity.Post;
import com.be_uterace.payload.response.ClubDetailResponse;
import com.be_uterace.payload.response.ClubPaginationResponse;
import com.be_uterace.payload.response.ClubResponse;
import com.be_uterace.payload.response.PostResponse;
import com.be_uterace.projection.ClubDetailProjection;
import com.be_uterace.projection.ClubProjection;
import com.be_uterace.projection.UserRankingProjection;
import com.be_uterace.repository.ClubRepository;
import com.be_uterace.repository.PostRepository;
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

    private PostRepository postRepository;

    public ClubServiceImpl(ClubRepository clubRepository, PostRepository postRepository) {
        this.clubRepository = clubRepository;
        this.postRepository = postRepository;
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

    @Override
    public ClubDetailResponse getClubDetail(int club_id) {
        ClubDetailProjection clubDetailProjection = clubRepository.getClubDetails(club_id);
        List<Post> postList = postRepository.findPostsCreatedByClubAdmin(club_id);
        List<PostResponse> postResponses = new ArrayList<>();
        for (Post item : postList){
            PostResponse response = new PostResponse();
            response.setNew_id(item.getPostId());
            response.setName(item.getTitle());
            response.setDescription(item.getDescription());
            response.setImage(item.getImage());
            response.setCreated_at(item.getCreatedAt());
            response.setUpdated_at(item.getUpdatedAt());
            postResponses.add(response);
        }
        return ClubDetailResponse.builder()
                .club_id(clubDetailProjection.getClubId())
                .image(clubDetailProjection.getPicturePath())
                .name(clubDetailProjection.getClubName())
                .description(clubDetailProjection.getDescription())
                .total_member(clubDetailProjection.getTotalMember())
                .total_distance(clubDetailProjection.getTotalDistance())
                .total_activities(clubDetailProjection.getTotalActivities())
                .news(postResponses)
                .created_at(clubDetailProjection.getCreatedAt())
                .manager(clubDetailProjection.getAdmin())
                .male(clubDetailProjection.getNumMales())
                .female(clubDetailProjection.getNumFemales())
                .min_pace(clubDetailProjection.getMinPace())
                .max_pace(clubDetailProjection.getMaxPace())
                .details(clubDetailProjection.getDetails())
                .build();
    }
}
