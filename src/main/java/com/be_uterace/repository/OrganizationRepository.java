package com.be_uterace.repository;

import com.be_uterace.entity.Organization;
import com.be_uterace.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationRepository extends JpaRepository<Organization,Long> {
}
