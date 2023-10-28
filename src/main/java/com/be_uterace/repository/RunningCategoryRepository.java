package com.be_uterace.repository;

import com.be_uterace.entity.Event;
import com.be_uterace.entity.RunningCategory;
import com.be_uterace.utils.key.RunningCategoryId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RunningCategoryRepository extends JpaRepository<RunningCategory, RunningCategoryId> {
    List<RunningCategory> findRunningCategoriesByEvent(Event event);
}
