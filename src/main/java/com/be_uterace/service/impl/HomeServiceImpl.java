package com.be_uterace.service.impl;

import com.be_uterace.entity.Club;
import com.be_uterace.entity.Event;
import com.be_uterace.entity.User;
import com.be_uterace.payload.response.HomePageResponse;
import com.be_uterace.projection.ClubRankingProjection;
import com.be_uterace.repository.ClubRepository;
import com.be_uterace.repository.EventRepository;
import com.be_uterace.repository.UserRepository;
import com.be_uterace.service.HomeService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class HomeServiceImpl implements HomeService {

    private EventRepository eventRepository;
    private ClubRepository clubRepository;
    private UserRepository userRepository;

    public HomeServiceImpl(EventRepository eventRepository, ClubRepository clubRepository, UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.clubRepository = clubRepository;
        this.userRepository = userRepository;
    }

    @Override
    public HomePageResponse getHomePage() {
        List<Map<String, Object>> overviewList = new ArrayList<>();
        List<Event> eventList = eventRepository.findAllByStatusAndEndDate();
        for (Event event : eventList) {
            Map<String, Object> overviewItem = new HashMap<>();
            overviewItem.put("event_id", event.getEventId());
            overviewItem.put("title", event.getTitle());
            overviewItem.put("content", event.getContent());
            overviewItem.put("member", event.getNumOfAttendee());
            overviewItem.put("club", event.getNumOfClubs());
            overviewList.add(overviewItem);
        }
        List<ClubRankingProjection> clubList = clubRepository.findTop8ClubsWithMemberAndEventCount();
        List<Map<String, Object>> rankingClubList = new ArrayList<>();

        for (ClubRankingProjection club : clubList) {
            Map<String, Object> rankingClubItem = new HashMap<>();

            rankingClubItem.put("club_id", club.getClubId());
            rankingClubItem.put("ranking", club.getClubRanking());
            rankingClubItem.put("name", club.getClubName());
            rankingClubItem.put("image", null);
            rankingClubItem.put("total_distance", club.getClubTotalDistance());
            rankingClubItem.put("total_members", club.getMemberCount());
            rankingClubItem.put("total_activities", club.getEventCount());
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
                .statistic(statistics).build();
    }

}