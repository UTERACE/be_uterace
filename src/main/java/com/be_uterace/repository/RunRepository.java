package com.be_uterace.repository;

import com.be_uterace.entity.Run;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RunRepository extends JpaRepository<Run,Long> {
    boolean existsByStravaRunId(Long aLong);
}
