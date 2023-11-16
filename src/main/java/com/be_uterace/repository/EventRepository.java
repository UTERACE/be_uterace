package com.be_uterace.repository;

import com.be_uterace.entity.Event;
import com.be_uterace.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public interface EventRepository extends JpaRepository<Event,Integer> {
    @Query("SELECT e FROM Event e WHERE e.status = '1' AND e.endDate > CURRENT_TIMESTAMP")
    List<Event> findAllByStatusAndEndDate();

    @Query("SELECT e FROM Event e WHERE e.status = :status AND e.endDate > CURRENT_TIMESTAMP " +
            "AND LOWER(e.title) LIKE %:search_name%") // Thêm điều kiện tìm kiếm
    Page<Event> findEventsWithStatusAndSearchName(
            @Param("status") String status,
            @Param("search_name") String search_name,
            Pageable pageable);

    Event findEventByEventId(Integer eventId);

    Page<Event> findEventByCreateUserAndTitleContaining( User createUser,String title, Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE Event e SET e.status = :mark WHERE e.eventId = :eventId")
    void markLockEvent(@Param("mark") String mark, @Param("eventId") Integer eventId);

    @Modifying
    @Transactional
    @Query("UPDATE Event e SET e.outstanding = :mark WHERE e.eventId = :eventId")
    void markOutstandingEvent(@Param("mark") String mark, @Param("eventId") Integer eventId);

}