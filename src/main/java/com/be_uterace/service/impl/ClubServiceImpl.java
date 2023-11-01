package com.be_uterace.service.impl;

import com.be_uterace.entity.Club;
import com.be_uterace.entity.Post;
import com.be_uterace.entity.User;
import com.be_uterace.entity.UserClub;
import com.be_uterace.payload.request.ClubAddDto;
import com.be_uterace.payload.request.ClubUpdateDto;
import com.be_uterace.payload.request.UserClubRequest;
import com.be_uterace.payload.response.*;
import com.be_uterace.projection.ClubDetailProjection;
import com.be_uterace.projection.ClubProjection;
import com.be_uterace.repository.ClubRepository;
import com.be_uterace.repository.PostRepository;
import com.be_uterace.repository.UserClubRepository;
import com.be_uterace.repository.UserRepository;
import com.be_uterace.service.ClubService;
import com.be_uterace.utils.StatusCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClubServiceImpl implements ClubService {

    private ClubRepository clubRepository;

    private PostRepository postRepository;

    private UserRepository userRepository;

    private UserClubRepository userClubRepository;

    public ClubServiceImpl(ClubRepository clubRepository, PostRepository postRepository, UserRepository userRepository, UserClubRepository userClubRepository) {
        this.clubRepository = clubRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.userClubRepository = userClubRepository;
    }

    @Override
    public ClubPaginationResponse getAllClub(int current_page, int per_page) {
        Pageable pageable = PageRequest.of(current_page - 1, per_page);
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
                .current_page(clubProjectionPage.getNumber()+1)
                .total_page(clubProjectionPage.getTotalPages())
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
            response.setNews_id(item.getPostId());
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
    public ResponseEntity<ResponseObject> createClub(ClubAddDto clubAddDto, Authentication authentication) {
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
                ResponseObject responseObject = new ResponseObject(StatusCode.SUCCESS,"Tạo clb thành công");
                return ResponseEntity.status(HttpStatus.OK).body(responseObject);
            }
        }
        ResponseObject responseObject = new ResponseObject(StatusCode.INTERNAL_SERVER_ERROR,"Tạo clb thất bại");
        return ResponseEntity.status(HttpStatus.OK).body(responseObject);

    }

    @Override
    public ResponseEntity<ResponseObject> updateClub(ClubUpdateDto clubUpdateDto, Authentication authentication) {
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
                    ResponseObject responseObject = new ResponseObject(StatusCode.SUCCESS,"Cập nhật clb thành công");
                    return ResponseEntity.status(HttpStatus.OK).body(responseObject);
                }
            }

        }
        ResponseObject responseObject = new ResponseObject(StatusCode.INTERNAL_SERVER_ERROR,"Cập nhật clb thất bại");
        return ResponseEntity.status(HttpStatus.OK).body(responseObject);
    }

    @Override
    public ResponseEntity<ResponseObject> deleteClub(Integer club_id, Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            String username = userDetails.getUsername();
            Optional<User> userOptional = userRepository.findByUsername(username);
            if (userOptional.isPresent()) {
                    clubRepository.deleteById(club_id);
                ResponseObject responseObject = new ResponseObject(StatusCode.SUCCESS,"Xóa clb thành công");
                return ResponseEntity.status(HttpStatus.OK).body(responseObject);
                }
            }

        ResponseObject responseObject = new ResponseObject(StatusCode.INVALID_ARGUMENT,"Xóa clb thất bại");
        return ResponseEntity.status(HttpStatus.OK).body(responseObject);
    }

    @Override
    public ResponseEntity<ResponseObject> deleteMember(UserClubRequest req) {
        Optional<UserClub> userClubOptional = userClubRepository.findByClubIdAndUserId(req.getClub_id(),req.getUser_id());
        if (userClubOptional.isPresent()) {
            userClubRepository.delete(userClubOptional.get());
            ResponseObject responseObject = new ResponseObject(StatusCode.SUCCESS,"Xóa thành viên thành công");
            return ResponseEntity.status(HttpStatus.OK).body(responseObject);
        }
        ResponseObject responseObject = new ResponseObject(StatusCode.INVALID_ARGUMENT,"Xóa thành viên thất bại");
        return ResponseEntity.status(HttpStatus.OK).body(responseObject);
    }

    @Override
    public ResponseEntity<ResponseObject> changeAdmin(UserClubRequest req) {
        Optional<UserClub> userClubOptional = userClubRepository.findByClubIdAndUserId(req.getClub_id(),req.getUser_id());
        if (userClubOptional.isPresent()) {
            Optional<Club> clubOptional = clubRepository.findById(req.getClub_id());
            if (clubOptional.isPresent()){
                Club club = clubOptional.get();
                club.setAdminUser(userClubOptional.get().getUser());
                clubRepository.save(club);
                ResponseObject responseObject = new ResponseObject(StatusCode.SUCCESS,"Đổi admin thành công");
                return ResponseEntity.status(HttpStatus.OK).body(responseObject);
            }
        }
        ResponseObject responseObject = new ResponseObject(StatusCode.INVALID_ARGUMENT,"Đổi admin thất bại");
        return ResponseEntity.status(HttpStatus.OK).body(responseObject);
    }

    @Override
    public ClubPaginationResponse getOwnClubCreated(int current_page, int per_page, Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            String username = userDetails.getUsername();
            Optional<User> userOptional = userRepository.findByUsername(username);
            if (userOptional.isPresent()) {
                Pageable pageable = PageRequest.of(current_page - 1, per_page);
                Page<ClubProjection> clubPage = clubRepository.findOwnClubPagination(pageable, userOptional.get().getUserId());
                List<ClubProjection> clubProjectionList = clubPage.getContent();
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
                        .per_page(clubPage.getSize())
                        .current_page(clubPage.getNumber() + 1)
                        .total_page(clubPage.getTotalPages())
                        .total_clubs((int) clubPage.getTotalElements())
                        .clubs(clubResponseList).build();
            }
        }
        return null;
    }

    @Override
    public ClubPaginationResponse getClubJoined(int current_page, int per_page, Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            String username = userDetails.getUsername();
            Optional<User> userOptional = userRepository.findByUsername(username);
            if (userOptional.isPresent()) {
                Pageable pageable = PageRequest.of(current_page - 1, per_page);
                Page<ClubProjection> clubPage = clubRepository.findClubJoined(pageable, userOptional.get().getUserId());
                List<ClubProjection> clubProjectionList = clubPage.getContent();
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
                        .per_page(clubPage.getSize())
                        .current_page(clubPage.getNumber() + 1)
                        .total_page(clubPage.getTotalPages())
                        .total_clubs((int) clubPage.getTotalElements())
                        .clubs(clubResponseList).build();
            }
        }
        return null;
    }

    @Override
    public ResponseObject joinClub(int club_id, Authentication authentication) {

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            String username = userDetails.getUsername();
            Optional<User> userOptional = userRepository.findByUsername(username);
            if (userOptional.isPresent()) {
                Optional<Club> clubOptional = clubRepository.findById(club_id);
                Optional<UserClub> userClubbool = userClubRepository.findByClubIdAndUserId(
                        clubOptional.get().getClubId(),userOptional.get().getUserId());
                if(userClubbool.isPresent()){
                    return new ResponseObject(StatusCode.INTERNAL_SERVER_ERROR,"User đã tham gia club này");

                }
                UserClub userClub = new UserClub();
                userClub.setClub(clubOptional.get());
                userClub.setUser(userOptional.get());
                userClubRepository.save(userClub);
                return new ResponseObject(StatusCode.SUCCESS,"Tham gia clb thành công");
            }
        }
        return new ResponseObject(StatusCode.INTERNAL_SERVER_ERROR,"Tham gia clb thất bại");
    }

    @Override
    public ResponseObject leaveClub(int club_id, Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            String username = userDetails.getUsername();
            Optional<User> userOptional = userRepository.findByUsername(username);
            if (userOptional.isPresent()) {
                Optional<UserClub> userClubOptional = userClubRepository.findByClubIdAndUserId(
                        club_id,userOptional.get().getUserId());
                userClubRepository.delete(userClubOptional.get());
                return new ResponseObject(StatusCode.SUCCESS,"Rời clb thành công");

            }
        }
        return new ResponseObject(StatusCode.INTERNAL_SERVER_ERROR,"Rời clb thất bại");
    }
}
