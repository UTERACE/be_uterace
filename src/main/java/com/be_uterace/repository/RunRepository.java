package com.be_uterace.repository;

import com.be_uterace.entity.Run;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RunRepository extends JpaRepository<Run,Long> {
    boolean existsByStravaRunId(Long aLong);
    List<Run> findAllByUser_UserId(Long userId);
}
