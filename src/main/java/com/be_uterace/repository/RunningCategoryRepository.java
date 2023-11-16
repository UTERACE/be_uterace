package com.be_uterace.repository;

import com.be_uterace.entity.Event;
import com.be_uterace.entity.RunningCategory;
import com.be_uterace.utils.key.RunningCategoryId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RunningCategoryRepository extends JpaRepository<RunningCategory, Integer> {
//    List<RunningCategory> findRunningCategoriesByEvent(Event event);
    @Query("SELECT rc FROM RunningCategory rc LEFT JOIN EventRunningCategory erc " +
            "ON rc.runningCategoryID = erc.runningCategory.runningCategoryID " +
            "WHERE erc.event.eventId = :eventId")
    List<RunningCategory> findRunningCategoriesByEvent_EventId(@Param("eventId") int eventId);
}
