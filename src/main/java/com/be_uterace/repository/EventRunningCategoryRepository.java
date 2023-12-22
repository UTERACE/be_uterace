package com.be_uterace.repository;

import com.be_uterace.entity.Event;
import com.be_uterace.entity.EventRunningCategory;
import com.be_uterace.entity.RunningCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRunningCategoryRepository extends JpaRepository<EventRunningCategory, Integer> {
    EventRunningCategory findEventRunningCategoryByEventAndRunningCategory(Event event, RunningCategory runningCategory);
    void deleteAllByEvent_EventId(Integer eventId);
}
