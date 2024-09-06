package com.orhanararat.repository;

import com.orhanararat.model.RuleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RuleRepository extends JpaRepository<RuleEntity, Long> {

    @Query("select count(r.priority) from RuleEntity r where r.priority = :priority and r.isDeleted = false")
    int findAllByPriorityCount(@Param("priority") Long priority);

    @Query("select r from RuleEntity r where r.isDeleted = false order by r.priority desc")
    List<RuleEntity> findAllRuleOrderByPriority();
}
