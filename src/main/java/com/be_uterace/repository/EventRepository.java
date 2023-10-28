package com.be_uterace.repository;

import com.be_uterace.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface EventRepository extends JpaRepository<Event,Long> {
    @Query("SELECT e FROM Event e WHERE e.status = '1' AND e.endDate > CURRENT_TIMESTAMP")
    List<Event> findAllByStatusAndEndDate();

    @Query("SELECT e FROM Event e WHERE e.status = :status AND e.endDate > CURRENT_TIMESTAMP")
    Page<Event> findAllByStatusAndEndDate(String status, Pageable pageable);

    Event findEventByEventId(Long eventId);





}