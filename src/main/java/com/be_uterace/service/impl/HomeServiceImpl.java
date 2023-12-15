package com.be_uterace.service.impl;

import com.be_uterace.entity.Club;
import com.be_uterace.entity.Event;
import com.be_uterace.entity.Post;
import com.be_uterace.entity.User;
import com.be_uterace.payload.response.*;
import com.be_uterace.projection.ClubRankingProjection;
import com.be_uterace.projection.UserRankingProjection;
import com.be_uterace.repository.ClubRepository;
import com.be_uterace.repository.EventRepository;
import com.be_uterace.repository.PostRepository;
import com.be_uterace.repository.UserRepository;
import com.be_uterace.service.HomeService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class HomeServiceImpl implements HomeService {

    private EventRepository eventRepository;
    private ClubRepository clubRepository;
    private PostRepository postRepository;
    private UserRepository userRepository;

    public HomeServiceImpl(EventRepository eventRepository, ClubRepository clubRepository,PostRepository postRepository, UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.clubRepository = clubRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Override
    public HomePageResponse getHomePage() {
//        List<Map<String, Object>> overviewList = new ArrayList<>();
//        List<Event> eventList = eventRepository.findEventsWithStatusOnGoing();
//        for (Event event : eventList) {
//            Map<String, Object> overviewItem = new HashMap<>();
//            overviewItem.put("event_id", event.getEventId());
//            overviewItem.put("title", event.getTitle());
//            overviewItem.put("content", event.getContent());
//            overviewItem.put("member", event.getNumOfAttendee());
//            overviewItem.put("club", event.getNumOfClubs());
//            overviewItem.put("image", event.getPicturePath());
//            overviewList.add(overviewItem);
//        }
        List<OverviewResponse> overview = eventRepository.findTop3EventsByOutstandingAndStatusAndNumOfAttendeeContaining();

//        List<ClubRankingResponse> rankingClub = clubRepository.findTop8ClubsByTotalDistanceContaining();
        List<ClubRankingProjection> rankingClub = clubRepository.findScoreboardTop8Club();
        List<ClubRankingResponse> rankingClubList = new ArrayList<>();
        rankingClub.stream().map(
                club -> ClubRankingResponse.builder()
                        .club_id(club.getC_club_id())
                        .ranking(club.getC_ranking())
                        .name(club.getC_club_name())
                        .image(club.getC_picture_path())
                        .total_distance(club.getC_total_distance())
                        .total_members(club.getC_num_of_attendee())
                        .total_activities(club.getC_total_activities())
                        .build()
        ).forEach(rankingClubList::add);


//        List<RankingUserHomeResponse> rankingUser = userRepository.findTop8ByOrderByTotalDistanceAsc();
        List<UserRankingProjection> rankingUser = userRepository.findScoreboardTop8User();
        List<RankingUserHomeResponse> rankingUserList = new ArrayList<>();
        rankingUser.stream().map(
                user -> RankingUserHomeResponse.builder()
                        .user_id(user.getU_user_id())
                        .ranking(user.getU_ranking())
                        .first_name(user.getU_firstname())
                        .last_name(user.getU_lastname())
                        .image(user.getU_avatar_path())
                        .total_distance(user.getU_total_distance())
                        .pace(user.getU_pace())
                        .build()
        ).forEach(rankingUserList::add);
        List<EventResponse> events = eventRepository.findTop6EventsByOutstandingAndStatusAndNumOfAttendeeContaining();

        List<ClubResponse> clubs = clubRepository.findTop6ClubsByOutstandingAndStatusAndNumOfAttendeeContaining();

        List<PostHomeResponse> posts = postRepository.findTop6ClubsByOutstandingAndCreatedAtContaining();

        Map<String, Object> statistics = new HashMap<>();

        // Sử dụng các repository để lấy thông tin thống kê
        Long totalMembers = userRepository.count();
        Double totalDistance = userRepository.sumTotalDistance();
        Long totalClubs = clubRepository.count();
        Long totalEvents = eventRepository.count();

        // Đặt giá trị vào Map
        statistics.put("total_members", totalMembers);
        statistics.put("total_distance", totalDistance);
        statistics.put("total_clubs", totalClubs);
        statistics.put("total_event", totalEvents);

        return HomePageResponse.builder()
                .overview(overview)
                .ranking_club(rankingClubList)
                .ranking_user(rankingUserList)
                .events(events)
                .clubs(clubs)
                .news(posts)
                .statistic(statistics).build();
    }

}