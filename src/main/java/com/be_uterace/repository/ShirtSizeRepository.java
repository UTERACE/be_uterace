package com.be_uterace.repository;

import com.be_uterace.entity.ShirtSize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShirtSizeRepository extends JpaRepository<ShirtSize,Integer> {
}
