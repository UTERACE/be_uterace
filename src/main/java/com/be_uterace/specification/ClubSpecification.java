package com.be_uterace.specification;

import com.be_uterace.entity.Club;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class ClubSpecification implements Specification<Club> {

    private SearchCriteria criteria;
    @Override
    public Predicate toPredicate
            (Root<Club> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return null;
    }
}
