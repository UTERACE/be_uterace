package com.be_uterace.service.impl;

import com.be_uterace.entity.*;
import com.be_uterace.exception.ResourceConflictException;
import com.be_uterace.payload.request.ClubAddDto;
import com.be_uterace.payload.request.ClubUpdateDto;
import com.be_uterace.payload.request.DeleteActivityClub;
import com.be_uterace.payload.request.UserClubRequest;
import com.be_uterace.payload.response.*;
import com.be_uterace.projection.ClubDetailProjection;
import com.be_uterace.projection.ClubProjection;
import com.be_uterace.repository.*;
import com.be_uterace.service.ClubService;
import com.be_uterace.service.FileService;
import com.be_uterace.utils.StatusCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ClubServiceImpl implements ClubService {

    private ClubRepository clubRepository;

    private PostRepository postRepository;

    private UserRepository userRepository;

    private UserClubRepository userClubRepository;

    private UCActivityRepository ucActivityRepository;
    private FileService fileService;

    public ClubServiceImpl(ClubRepository clubRepository, PostRepository postRepository, UserRepository userRepository,
                           UserClubRepository userClubRepository,
                           FileService fileService,
                           UCActivityRepository ucActivityRepository) {
        this.clubRepository = clubRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.userClubRepository = userClubRepository;
        this.fileService = fileService;
        this.ucActivityRepository = ucActivityRepository;
    }

    @Override
    public ClubPaginationResponse getAllClub(int current_page, int per_page, String search) {
        Pageable pageable = PageRequest.of(current_page - 1, per_page);
        Page<ClubProjection> clubProjectionPage = clubRepository.findAllClubPagination(search ,pageable);
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
//        List<Post> postList = postRepository.findPostsCreatedByClubAdmin(club_id);
        List<Post> postList = postRepository.getPostsByClubClubId(club_id);
        List<PostResponse> postResponses = new ArrayList<>();
        for (Post item : postList){
            PostResponse response = new PostResponse();
            response.setNews_id(item.getPostId());
            response.setName(item.getTitle());
            response.setDescription(item.getDescription());
            response.setImage(item.getImage());
            response.setCreated_at(item.getCreatedAt());
            response.setUpdated_at(item.getUpdatedAt());
            response.setStatus(item.getStatus());
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
                .total_news(postList.size())
                .build();
    }

    @Override
    public ResponseObject createClub(ClubAddDto clubAddDto, Authentication authentication) {
        clubRepository.findClubByClubName(clubAddDto.getName())
                .ifPresent(club -> {
                    throw new ResourceConflictException("Club name");
                });

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            String username = userDetails.getUsername();
            Optional<User> userOptional = userRepository.findByUsername(username);
            if (userOptional.isPresent()) {
                Club club = new Club();
                club.setClubName(clubAddDto.getName());
                club.setDescription(clubAddDto.getDescription());
                if (!Objects.equals(clubAddDto.getImage(), ""))
                    club.setPicturePath(fileService.saveImage(clubAddDto.getImage()));
                else
                    club.setPicturePath("");
                club.setDetails(clubAddDto.getDetails());
                club.setMinPace(clubAddDto.getMin_pace());
                club.setMaxPace(clubAddDto.getMax_pace());
                club.setAdminUser(userOptional.get());
                club.setCreatorUser(userOptional.get());
                clubRepository.save(club);
                UserClub userClub = new UserClub();
                userClub.setClub(club);
                userClub.setUser(userOptional.get());
                userClubRepository.save(userClub);
                ResponseObject responseObject = new ResponseObject(StatusCode.SUCCESS,"Tạo clb thành công");
                return responseObject;
            }
        }
        ResponseObject responseObject = new ResponseObject(StatusCode.INTERNAL_SERVER_ERROR,"Tạo clb thất bại");
        return responseObject;

    }

    @Override
    public ResponseEntity<ResponseObject> updateClub(ClubUpdateDto clubUpdateDto, Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(StatusCode.UNAUTHORIZED,"Bạn không có quyền thực hiện hành động này"));
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(StatusCode.NOT_FOUND,"Không tìm thấy user"));
        }
        Optional<Club> clubOptional = clubRepository.findById(clubUpdateDto.getClub_id());
        if (clubOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(StatusCode.NOT_FOUND,"Không tìm thấy clb"));
        }
        Club club = clubOptional.get();
        club.setClubName(clubUpdateDto.getName() != null && !Objects.equals(clubUpdateDto.getName(), "") ? clubUpdateDto.getName() : club.getClubName());
        club.setDescription(clubUpdateDto.getDescription() != null && !Objects.equals(clubUpdateDto.getDescription(), "") ? clubUpdateDto.getDescription() : club.getDescription());
        if (!club.getPicturePath().equals(clubUpdateDto.getImage()) && !Objects.equals(clubUpdateDto.getImage(), "")){
            if (Objects.equals(club.getPicturePath(), ""))
                club.setPicturePath(fileService.saveImage(clubUpdateDto.getImage()));
            else if (fileService.deleteImage(club.getPicturePath())){
                System.out.println("Delete Image Successful");
                club.setPicturePath(fileService.saveImage(clubUpdateDto.getImage()));
            }
        }
        club.setDetails(clubUpdateDto.getDetails() != null && !Objects.equals(clubUpdateDto.getDetails(), "") ? clubUpdateDto.getDetails() : club.getDetails());
        club.setMinPace(clubUpdateDto.getMin_pace() != null ? clubUpdateDto.getMin_pace() : club.getMinPace());
        club.setMaxPace(clubUpdateDto.getMax_pace() != null ? clubUpdateDto.getMax_pace() : club.getMaxPace());
        clubRepository.save(club);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(StatusCode.SUCCESS,"Cập nhật clb thành công"));
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
    public ClubPaginationResponse getOwnClubCreated(int current_page, int per_page, String search, Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            String username = userDetails.getUsername();
            Optional<User> userOptional = userRepository.findByUsername(username);
            if (userOptional.isPresent()) {
                Pageable pageable = PageRequest.of(current_page - 1, per_page);
                Page<ClubProjection> clubPage = clubRepository.findOwnClubPagination(search ,pageable, userOptional.get().getUserId());
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
    public ClubPaginationResponse getClubJoined(int current_page, int per_page, String search, Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            String username = userDetails.getUsername();
            Optional<User> userOptional = userRepository.findByUsername(username);
            if (userOptional.isPresent()) {
                Pageable pageable = PageRequest.of(current_page - 1, per_page);
                Page<ClubProjection> clubPage = clubRepository.findClubJoined(search, pageable, userOptional.get().getUserId());
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
    public ClubPaginationResponse getClubCreatedByUser(Long user_id, int current_page, int per_page, String search, Authentication authentication) {
        Pageable pageable = PageRequest.of(current_page - 1, per_page);
        Page<ClubProjection> clubPage = clubRepository.findOwnClubPagination(search, pageable, user_id);
        List<ClubProjection> clubProjectionList = clubPage.getContent();
        List<ClubResponse> clubResponseList = new ArrayList<>();
        for (ClubProjection item : clubProjectionList) {
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
                userClub.setJoinDate(new Timestamp(System.currentTimeMillis()));
                userClub.setTotalDistance(0.0);
                userClub.setPace(0.0);
                userClubRepository.save(userClub);
                Club club = clubOptional.get();
                club.setNumOfAttendee(club.getNumOfAttendee() + 1);
                if (Objects.equals(userOptional.get().getGender(), "Nam"))
                    club.setNumOfMales(club.getNumOfMales() + 1);
                else
                    club.setNumOfFemales(club.getNumOfFemales() + 1);
                clubRepository.save(club);
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
                Club club = clubRepository.findById(club_id).get();
                club.setNumOfAttendee(club.getNumOfAttendee() - 1);
                if (Objects.equals(userOptional.get().getGender(), "Nam") && club.getNumOfMales() > 0)
                    club.setNumOfMales(club.getNumOfMales() - 1);
                else if (Objects.equals(userOptional.get().getGender(), "Nữ") && club.getNumOfFemales() > 0)
                    club.setNumOfFemales(club.getNumOfFemales() - 1);
                clubRepository.save(club);
                return new ResponseObject(StatusCode.SUCCESS,"Rời clb thành công");

            }
        }
        return new ResponseObject(StatusCode.INTERNAL_SERVER_ERROR,"Rời clb thất bại");
    }

    @Override
    public Boolean checkJoinClub(int club_id, Authentication auth) {
        if (auth != null && auth.getPrincipal() instanceof UserDetails userDetails) {
            String username = userDetails.getUsername();
            Optional<User> userOptional = userRepository.findByUsername(username);
            if (userOptional.isPresent()) {
                Optional<UserClub> userClubOptional = userClubRepository.findByClubIdAndUserId(
                        club_id,userOptional.get().getUserId());
                return userClubOptional.isPresent();
            }
        }
        return false;
    }

    @Override
    @Transactional
    public ResponseObject deleteActivity(DeleteActivityClub req) {
        int updatedRows = ucActivityRepository.updateStatusAndReasonByClubIdAndRunId(req.getReason(),req.getClub_id(),req.getActivity_id());

        return Optional.of(updatedRows)
                .filter(rows -> rows > 0)
                .map(rows -> new ResponseObject(StatusCode.SUCCESS, "Xóa hoạt động thành công"))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Club hoặc Activity không tồn tại"));
    }

    @Override
    public RecentActiveResponse getRecentActivity(int current_page, int per_page, Integer clubId, String search, int hours) {

        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Club not found"));
        Page<UserClubActivity> activityPage;
        Pageable pageable = PageRequest.of(current_page - 1, per_page);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime thresholdDateTime = now.minusHours(hours);
        Timestamp thresholdTimestamp = Timestamp.valueOf(thresholdDateTime);

        if(search==null || search.isEmpty()) {
            activityPage = ucActivityRepository.findActivityByDateTimeAndName(thresholdTimestamp, null, clubId,pageable);
        } else {
            activityPage = ucActivityRepository.findActivityByDateTimeAndName(thresholdTimestamp, search,clubId,pageable);
        }
        List<UserClubActivity> userClubActivityList = activityPage.getContent();
        List<RecentActiveResponse.Activity> arrayList = new ArrayList<>();
        for (UserClubActivity userClubActivity : userClubActivityList){
            Run run = userClubActivity.getRun();
            RecentActiveResponse.Activity activityResponse = new RecentActiveResponse.Activity();
            activityResponse.setActivity_id(userClubActivity.getId());
            activityResponse.setMember_id(run.getUser().getUserId());
            activityResponse.setMember_image(run.getUser().getAvatarPath());
            activityResponse.setMember_name(run.getUser().getFirstName()+" "+run.getUser().getLastName());
            activityResponse.setActivity_start_date(run.getCreatedAt());
            activityResponse.setActivity_distance(run.getDistance());
            activityResponse.setActivity_pace(run.getPace());
            activityResponse.setActivity_duration(run.getDuration());
            activityResponse.setActivity_name(run.getName());
            activityResponse.setActivity_type(run.getType());
            activityResponse.setActivity_link_strava("https://www.strava.com/activities/"+run.getStravaRunId());
            activityResponse.setActivity_map(run.getSummaryPolyline());
            activityResponse.setStatus(run.getStatus());
            activityResponse.setReason(run.getReason());
            arrayList.add(activityResponse);
        }
        return RecentActiveResponse.builder()
                .per_page(activityPage.getSize())
                .total_activities((int) activityPage.getTotalElements())
                .current_page(activityPage.getNumber() + 1)
                .total_page(activityPage.getTotalPages())
                .activities(arrayList)
                .build();
    }

    @Override
    public RankingMemberResponse getScoreBoardClubMember(int club_id, int current_page, int per_page, String search_name) {
        Pageable pageable = PageRequest.of(current_page-1, per_page);
        clubRepository.findById(club_id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Club not found"));
        Page<UserClub> userClubPage;
        if(search_name==null || search_name.isEmpty()){
            userClubPage = userClubRepository.findByClubIdAndSearchName(club_id,null,pageable);
        }
        else userClubPage = userClubRepository.findByClubIdAndSearchName(club_id, search_name,pageable);
        List<UserClub> userClubList = userClubPage.getContent();

        List<RankingMemberResponse.RankingUser> rankingUserList = new ArrayList<>();
        for (UserClub userClub : userClubList){
            RankingMemberResponse.RankingUser rankingUser = new RankingMemberResponse.RankingUser();
            User user = userClub.getUser();
            rankingUser.setUser_id(user.getUserId());
            rankingUser.setRanking(userClub.getRanking());
            rankingUser.setFirst_name(user.getFirstName());
            rankingUser.setLast_name(user.getLastName());
            rankingUser.setGender(user.getGender().name());
            rankingUser.setPace(userClub.getPace());
            rankingUser.setImage(user.getAvatarPath());
            rankingUser.setTotal_distance(userClub.getTotalDistance());
            rankingUser.setJoin_date(userClub.getJoinDate());
            rankingUserList.add(rankingUser);
        }
        return RankingMemberResponse.builder()
                .per_page(userClubPage.getSize())
                .total_user((int) userClubPage.getTotalElements())
                .current_page(userClubPage.getNumber()+1)
                .total_page(userClubPage.getTotalPages())
                .ranking_user(rankingUserList)
                .build();
    }
}
