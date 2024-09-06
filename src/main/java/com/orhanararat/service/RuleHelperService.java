package com.orhanararat.service;

import com.orhanararat.model.RuleEntity;
import com.orhanararat.repository.RuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RuleHelperService {
    private final RuleRepository ruleRepository;


    @Cacheable(value = "allRule", sync = true)
    public List<RuleEntity> getAllRuleEntities() {
        return ruleRepository.findAllRuleOrderByPriority();
    }

    @CacheEvict(value = "allRule")
    public synchronized void evictAllRuleEntities() {
        //
    }
}
