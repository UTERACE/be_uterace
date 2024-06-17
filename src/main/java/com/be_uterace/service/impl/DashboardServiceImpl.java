package com.be_uterace.service.impl;

import com.be_uterace.payload.response.DashboardResponse;
import com.be_uterace.repository.*;
import com.be_uterace.service.DashboardService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class DashboardServiceImpl implements DashboardService {
    private final ClubRepository clubRepository;
    private final EventRepository eventRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final RunRepository runRepository;

    public DashboardServiceImpl(ClubRepository clubRepository, EventRepository eventRepository, PostRepository postRepository, UserRepository userRepository, RunRepository runRepository) {
        this.clubRepository = clubRepository;
        this.eventRepository = eventRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.runRepository = runRepository;
    }

    @Override
    public DashboardResponse getDashboard() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -7);
        Date dateBefore7Days = calendar.getTime();

        long count_clubs = clubRepository.count();
        long count_events = eventRepository.count();
        long count_news = postRepository.count();
        long count_users = userRepository.count();

        long count_clubs_last_week = clubRepository.countByCreatedAtBetween(dateBefore7Days, new Date());
        long count_events_last_week = eventRepository.countByCreateAtBetween(dateBefore7Days, new Date());
        long count_news_last_week = postRepository.countByCreatedAtBetween(dateBefore7Days, new Date());
        long count_users_last_week = userRepository.countByCreatedAtBetween(dateBefore7Days, new Date());

        List<Object[]> rawActivities = runRepository.chartActivity();
        List<Integer> chart_active_users = getChart(rawActivities, 3);

        List<Object[]> rawEvents = eventRepository.chartEvents();
        List<Integer> chart_events = getChart(rawEvents, 6);

        List<Object[]> rawNews = postRepository.chartPosts();
        List<Integer> chart_news = getChart(rawNews, 6);

        List<Integer> chart_users = new ArrayList<>();
        chart_users.add(eventRepository.countByEndDateAndStatus());
        chart_users.add(eventRepository.countByStartDateAndStatus());
        chart_users.add(eventRepository.countByStartDateAndEndDateAndStatus());
        chart_users.add(eventRepository.countByStartDateAndEndDateAndStatusAndOutstanding());
        chart_users.add(eventRepository.countByStatus());

        List<Object[]> rawClubs = clubRepository.chartClubs();
        List<Integer> chart_clubs = getChart(rawClubs, 6);

        return DashboardResponse.builder()
                .total_clubs((int) count_clubs)
                .new_clubs((int) count_clubs_last_week)
                .total_events((int) count_events)
                .new_events((int) count_events_last_week)
                .total_news((int) count_news)
                .new_news((int) count_news_last_week)
                .total_users((int) count_users)
                .new_users((int) count_users_last_week)
                .chart_active_users(chart_active_users)
                .chart_clubs(chart_clubs)
                .chart_news(chart_news)
                .chart_events_status(chart_users)
                .chart_events(chart_events)
                .build();
    }

    public List<Integer> getChart(List<Object[]> raw, int month_count) {
        // Initialize the map with 0 counts for the last 4 months
        Map<String, Integer> monthCountMap = new HashMap<>();
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

        for (int i = month_count; i >= 0; i--) {
            LocalDate month = currentDate.minusMonths(i);
            monthCountMap.put(month.format(formatter), 0);
        }

        // Populate the map with counts from the database
        for (Object[] record : raw) {
            String month = ((java.sql.Timestamp) record[0]).toLocalDateTime().toLocalDate().format(formatter);
            Integer count = ((Number) record[1]).intValue();
            monthCountMap.put(month, count);
        }

        // Convert the map to a list in the correct order
        List<Integer> chart = new ArrayList<>();
        for (int i = month_count; i >= 0; i--) {
            LocalDate month = currentDate.minusMonths(i);
            chart.add(monthCountMap.get(month.format(formatter)));
        }
        return chart;
    }
}
