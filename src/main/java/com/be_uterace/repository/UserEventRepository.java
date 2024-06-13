package com.be_uterace.repository;

import com.be_uterace.entity.Event;
import com.be_uterace.entity.UserEvent;
import com.be_uterace.utils.key.UserEventId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserEventRepository extends JpaRepository<UserEvent, UserEventId> {
    @Query("SELECT ue FROM UserEvent ue WHERE ue.user.userId = :userId AND ue.event.eventId = :eventId")
    Optional<UserEvent> findByUserUserIdAndEventEventId(@Param("userId") Long userId, @Param("eventId") Integer eventId);

    @Query("SELECT COUNT(ue) FROM UserEvent ue WHERE ue.user.userId = :userId AND ue.status = '1'")
    int countEventsByUserId(@Param("userId") Long userId);

    Page<UserEvent> findAllByEvent(Event event, Pageable pageable);

    @Query("SELECT ue FROM UserEvent ue " +
            "INNER JOIN ue.user u " +
            "WHERE ue.event.eventId = :eventId " +
            "AND ue.status = '1' " +
            "AND (:searchName IS NULL " +
            "OR (unaccent(LOWER(u.firstName)) ILIKE unaccent(LOWER(:searchName)) " +
            "OR unaccent(LOWER(u.lastName)) ILIKE unaccent(LOWER(:searchName))))")
    Page<UserEvent> findByEventIdAndSearchName(
            @Param("eventId") Integer eventId,
            @Param("searchName") String searchName,
            Pageable pageable
    );
}
