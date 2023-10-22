package com.be_uterace.service.impl;

import ch.qos.logback.classic.spi.IThrowableProxy;
import com.be_uterace.entity.Club;
import com.be_uterace.entity.Post;
import com.be_uterace.entity.User;
import com.be_uterace.payload.request.ClubAddDto;
import com.be_uterace.payload.request.ClubUpdateDto;
import com.be_uterace.payload.response.*;
import com.be_uterace.projection.ClubDetailProjection;
import com.be_uterace.projection.ClubProjection;
import com.be_uterace.projection.UserRankingProjection;
import com.be_uterace.repository.ClubRepository;
import com.be_uterace.repository.PostRepository;
import com.be_uterace.repository.UserRepository;
import com.be_uterace.service.ClubService;
import com.be_uterace.utils.StatusCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.be_uterace.utils.DateConverter.convertStringToDate;

@Service
public class ClubServiceImpl implements ClubService {

    private ClubRepository clubRepository;

    private PostRepository postRepository;

    private UserRepository userRepository;

    public ClubServiceImpl(ClubRepository clubRepository, PostRepository postRepository, UserRepository userRepository) {
        this.clubRepository = clubRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
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

    @Override
    public ResponseObject createClub(ClubAddDto clubAddDto, Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            String username = userDetails.getUsername();
            Optional<User> userOptional = userRepository.findByUsername(username);
            if (userOptional.isPresent()) {
                Club club = new Club();
                club.setClubName(clubAddDto.getName());
                club.setDescription(clubAddDto.getDescription());
                club.setPicturePath(clubAddDto.getImage());
                club.setDetails(clubAddDto.getDetails());
                club.setMinPace(clubAddDto.getMin_pace());
                club.setMaxPace(clubAddDto.getMax_pace());
                club.setAdminUser(userOptional.get());
                club.setCreatorUser(userOptional.get());
                clubRepository.save(club);
                return ResponseObject.builder()
                        .status(StatusCode.CREATED)
                        .message("Tạo clb thành công").build();
            }
        }
        return ResponseObject.builder()
                .status(StatusCode.INTERNAL_SERVER_ERROR)
                .message("Loi").build();

    }

    @Override
    public ResponseObject updateClub(ClubUpdateDto clubUpdateDto, Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            String username = userDetails.getUsername();
            Optional<User> userOptional = userRepository.findByUsername(username);
            if (userOptional.isPresent()) {
                Optional<Club> clubOptional = clubRepository.findById(clubUpdateDto.getClub_id());
                if (clubOptional.isPresent()){
                    Club club = clubOptional.get();
                    club.setClubName(clubUpdateDto.getName());
                    club.setDescription(clubUpdateDto.getDescription());
                    club.setPicturePath(clubUpdateDto.getImage());
                    //club.setDetails(club.getDetails());
                    club.setMinPace(clubUpdateDto.getMin_pace());
                    club.setMaxPace(clubUpdateDto.getMax_pace());
                    clubRepository.save(club);
                    return ResponseObject.builder()
                            .status(StatusCode.SUCCESS)
                            .message("Cập nhật thông tin clb thành công").build();
                }
            }

        }
        return ResponseObject.builder()
                .status(StatusCode.INTERNAL_SERVER_ERROR)
                .message("Loi").build();
    }

    @Override
    public ResponseObject deleteClub(Integer club_id, Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            String username = userDetails.getUsername();
            Optional<User> userOptional = userRepository.findByUsername(username);
            if (userOptional.isPresent()) {
                    clubRepository.deleteById(club_id);
                    return ResponseObject.builder()
                            .status(StatusCode.SUCCESS)
                            .message("Xóa clb thành công").build();
                }
            }

        return ResponseObject.builder()
                .status(StatusCode.INTERNAL_SERVER_ERROR)
                .message("Loi").build();
    }
}
