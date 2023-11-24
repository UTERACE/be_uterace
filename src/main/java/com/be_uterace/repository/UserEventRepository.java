package com.be_uterace.repository;

import com.be_uterace.entity.UserEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserEventRepository extends JpaRepository<UserEvent,Integer> {
    Optional<UserEvent> findByUserUserIdAndEventEventId(Long user_userId, Integer event_eventId);

    @Query("SELECT COUNT(ue) FROM UserEvent ue WHERE ue.user.userId = :userId")
    int countEventsByUserId(@Param("userId") Long userId);
}
