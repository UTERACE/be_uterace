package com.be_uterace.repository;

import com.be_uterace.entity.Club;
import com.be_uterace.entity.Event;
import com.be_uterace.entity.User;
import com.be_uterace.payload.response.EventResponse;
import com.be_uterace.payload.response.OverviewResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Integer> {

    Optional<Event> findEventByEventIdAndAdminUser_UserId(Integer eventId, Long userId);

    @Query("SELECT e FROM Event e WHERE e.startDate < CURRENT_TIMESTAMP AND e.endDate > CURRENT_TIMESTAMP " +
            "ORDER BY e.endDate DESC")
        // Thêm điều kiện tìm kiếm
    List<Event> findEventsWithStatusOnGoing();

    List<Event> findTop6EventsByOutstanding(String outstanding);

    @Query("SELECT e FROM Event e WHERE e.startDate < CURRENT_TIMESTAMP AND e.endDate > CURRENT_TIMESTAMP " +
            " AND unaccent(LOWER(e.title)) LIKE unaccent(LOWER(concat('%', :search_name, '%'))) " +
            " ORDER BY e.createAt DESC ")
        // Thêm điều kiện tìm kiếm
    Page<Event> findEventsWithStatusOnGoing(
            @Param("search_name") String search_name,
            Pageable pageable);

    @Query("SELECT e FROM Event e WHERE e.startDate < CURRENT_TIMESTAMP AND e.endDate > CURRENT_TIMESTAMP " +
            " AND e.eventId=:eventId ")
        // Thêm điều kiện tìm kiếm
    Optional<Event> findEventsWithStatusOnGoing(@Param("eventId") Integer eventId);

    @Query("SELECT e FROM Event e WHERE e.endDate < CURRENT_TIMESTAMP " +
            "AND unaccent(LOWER(e.title)) LIKE unaccent(LOWER(concat('%', :search_name, '%'))) "
            + "ORDER BY e.endDate DESC")
        // Thêm điều kiện tìm kiếm
    Page<Event> findEventsWithStatusFinished(
            @Param("search_name") String search_name,
            Pageable pageable);

    @Query("SELECT e FROM Event e WHERE e.startDate > CURRENT_TIMESTAMP " +
            "AND unaccent(LOWER(e.title)) LIKE unaccent(LOWER(concat('%', :search_name, '%'))) "
            + "ORDER BY e.startDate ASC")
        // Thêm điều kiện tìm kiếm
    Page<Event> findEventsWithStatusUpcoming(
            @Param("search_name") String search_name,
            Pageable pageable);


    Event findEventByEventId(Integer eventId);

    @Query("SELECT e FROM Event e WHERE e.createUser.userId = :user_id " +
            "AND unaccent(LOWER(e.title)) LIKE unaccent(LOWER(concat('%', :search_name, '%'))) ")
    Page<Event> findEventByCreateUserAndTitleContaining(
            @Param("user_id") Long user_id,
            @Param("search_name") String search_name,
            Pageable pageable);

    @Query("SELECT e FROM Event e LEFT JOIN UserEvent ue ON e.eventId = ue.event.eventId " +
            "WHERE ue.user.userId = :userId AND " +
            "unaccent(LOWER(e.title)) LIKE unaccent(LOWER(concat('%', :search_name, '%'))) ")
    Page<Event> findEventByJoinUserUserId(@Param("search_name") String search_name, Pageable pageable, @Param("userId") Long userId);

    @Query("SELECT new com.be_uterace.payload.response.EventResponse(e.eventId, e.title, e.picturePath, e.numOfAttendee, e.totalActivities) " +
            "FROM Event e " +
            "WHERE e.outstanding = '1' AND e.status = '1' AND e.numOfAttendee >= 0 " +
            "ORDER BY e.numOfAttendee DESC " +
            "LIMIT 6")
    List<EventResponse> findTop6EventsByOutstandingAndStatusAndNumOfAttendeeContaining();

    @Query("SELECT new com.be_uterace.payload.response.OverviewResponse(e.eventId, e.title,e.content, e.picturePath, e.numOfAttendee, e.totalActivities) " +
            "FROM Event e " +
            "WHERE e.outstanding = '1' AND e.status = '1' AND e.numOfAttendee >= 0 " +
            "ORDER BY e.totalActivities DESC " +
            "LIMIT 3")
    List<OverviewResponse> findTop3EventsByOutstandingAndStatusAndNumOfAttendeeContaining();

    @Modifying
    @Transactional
    @Query("UPDATE Event e SET e.status = :mark, e.reason = :reason WHERE e.eventId = :eventId")
    void markLockEvent(@Param("mark") String mark, @Param("eventId") Integer eventId, @Param("reason") String reason);

    @Modifying
    @Transactional
    @Query("UPDATE Event e SET e.outstanding = :mark WHERE e.eventId = :eventId")
    void markOutstandingEvent(@Param("mark") String mark, @Param("eventId") Integer eventId);

    @Query("SELECT e FROM Event e WHERE unaccent(LOWER(e.title)) LIKE unaccent(LOWER(concat('%', :searchName, '%')))")
    Page<Event> searchEventManage(@Param("searchName") String searchName, Pageable pageable);


    @Query("SELECT e FROM Event e " +
            "INNER JOIN UserEvent ue ON e.eventId = ue.event.eventId " +
            "WHERE e.startDate <= CURRENT_DATE AND e.endDate >= CURRENT_DATE " +
            "AND (:userId IS NULL OR ue.user.userId = :userId) " +  // Kiểm tra user_id có giá trị hay không
            "AND unaccent(LOWER(e.title)) LIKE unaccent(LOWER(concat('%', :searchName, '%'))) " +
            "ORDER BY e.createAt")
    Page<Event> getEventInCurrentTime(Pageable pageable, String searchName, Long userId);

    @Query("SELECT e FROM Event e WHERE NOT (e.startDate <= CURRENT_DATE AND e.endDate >= CURRENT_DATE) AND unaccent(LOWER(e.title)) LIKE unaccent(LOWER(concat('%', :searchName, '%'))) ORDER BY e.createAt"
    )
    Page<Event> getEventNotInCurrentTime(Pageable pageable);

    @Query("SELECT e FROM Event e " +
            "INNER JOIN UserEvent ue ON e.eventId = ue.event.eventId " +
            "WHERE e.endDate < CURRENT_DATE " +
            "AND (:userId IS NULL OR ue.user.userId = :userId) " +  // Kiểm tra user_id có giá trị hay không
            "AND unaccent(LOWER(e.title)) LIKE unaccent(LOWER(concat('%', :searchName, '%'))) " +
            "ORDER BY e.createAt")
    Page<Event> getEventEnded(Pageable pageable, String searchName, Long userId);

    long countByCreateAtBetween(Date start, Date end);

    @Query(nativeQuery = true,
            value = "SELECT DATE_TRUNC('month', r.create_at) AS month, COUNT(*) AS count " +
                    "FROM Event r " +
                    "WHERE r.create_at >= DATE_TRUNC('month', CURRENT_DATE) - INTERVAL '6 months' " +
                    "GROUP BY month " +
                    "ORDER BY month DESC")
    List<Object[]> chartEvents();

    @Query("SELECT COUNT(*) FROM Event e WHERE e.endDate < CURRENT_TIMESTAMP AND e.status = '1'")
    int countByEndDateAndStatus();

    @Query("SELECT COUNT(*) FROM Event e WHERE e.endDate >= CURRENT_TIMESTAMP AND e.startDate <= CURRENT_TIMESTAMP AND e.status = '1'")
    int countByStartDateAndEndDateAndStatus();

    @Query("SELECT COUNT(*) FROM Event e WHERE e.startDate > CURRENT_TIMESTAMP AND e.status = '1'")
    int countByStartDateAndStatus();

    @Query("SELECT COUNT(*) FROM Event e WHERE e.outstanding = '1' AND e.status = '1'")
    int countByStartDateAndEndDateAndStatusAndOutstanding();

    @Query("SELECT COUNT(*) FROM Event e WHERE e.status = '0'")
    int countByStatus();
}