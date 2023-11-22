package com.be_uterace.service.impl;

import com.be_uterace.entity.Club;
import com.be_uterace.entity.Event;
import com.be_uterace.entity.Post;
import com.be_uterace.entity.User;
import com.be_uterace.payload.response.ClubRankingResponse;
import com.be_uterace.payload.response.HomePageResponse;
import com.be_uterace.projection.ClubRankingProjection;
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
        List<Map<String, Object>> overviewList = new ArrayList<>();
        List<Event> eventList = eventRepository.findEventsWithStatusOnGoing();
        for (Event event : eventList) {
            Map<String, Object> overviewItem = new HashMap<>();
            overviewItem.put("event_id", event.getEventId());
            overviewItem.put("title", event.getTitle());
            overviewItem.put("content", event.getContent());
            overviewItem.put("member", event.getNumOfAttendee());
            overviewItem.put("club", event.getNumOfClubs());
            overviewItem.put("image", event.getPicturePath());
            overviewList.add(overviewItem);
        }
        List<ClubRankingProjection> clubList = clubRepository.findTop8ClubsWithMemberAndEventCount();
        List<ClubRankingResponse> rankingClubList = new ArrayList<>();

        for (ClubRankingProjection club : clubList) {
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


        List<User> userList = userRepository.findTop8ByOrderByRankingAsc();
        List<Map<String, Object>> userRanking = new ArrayList<>();
        for (User user : userList) {
            Map<String, Object> userJson = new HashMap<>();
            userJson.put("user_id", user.getUserId());
            userJson.put("ranking", user.getRanking());
            userJson.put("first_name", user.getFirstName());
            userJson.put("last_name", user.getLastName());
            userJson.put("image", null);
            userJson.put("pace", user.getPace());
            userJson.put("total_distance", user.getTotalDistance());
            userJson.put("organization", null);

            userRanking.add(userJson);

        }
        List<Map<String, Object>> eventsList = new ArrayList<>();
        List<Event> events = eventRepository.findTop6EventsByOutstanding("1");
        for (Event event : events) {
            Map<String, Object> eventItem = new HashMap<>();
            eventItem.put("event_id", event.getEventId());
            eventItem.put("name", event.getTitle());
            eventItem.put("image", event.getPicturePath());
            eventItem.put("total_members", event.getNumOfAttendee());
            eventItem.put("total_clubs", event.getNumOfClubs());
            eventsList.add(eventItem);
        }

        List<Map<String, Object>> clubsList = new ArrayList<>();
        List<Club> clubs = clubRepository.findTop6ClubsByOutstanding("1");
        for (Club club : clubs) {
            Map<String, Object> clubItem = new HashMap<>();
            clubItem.put("club_id", club.getClubId());
            clubItem.put("name", club.getClubName());
            clubItem.put("image", club.getPicturePath());
            clubItem.put("total_members", club.getTotalActivities());
            clubItem.put("total_distance", club.getClubTotalDistance());
            clubsList.add(clubItem);
        }

        List<Map<String, Object>> postsList = new ArrayList<>();
        List<Post> posts = postRepository.findTop6PostsByOutstanding("1");
        for (Post post : posts) {
            Map<String, Object> postItem = new HashMap<>();
            postItem.put("news_id", post.getPostId());
            postItem.put("name", post.getTitle());
            postItem.put("description", post.getDescription());
            postItem.put("image", post.getImage());
            postsList.add(postItem);
        }

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
                .overview(overviewList)
                .ranking_club(rankingClubList)
                .ranking_user(userRanking)
                .events(eventsList)
                .clubs(clubsList)
                .news(postsList)
                .statistic(statistics).build();
    }

}