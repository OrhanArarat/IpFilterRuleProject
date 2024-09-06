package com.orhanararat.service;

import com.orhanararat.dto.request.CheckRequestDto;
import com.orhanararat.dto.request.RuleRequestDto;
import com.orhanararat.dto.response.RuleResponseDto;
import com.orhanararat.exception.NonUniquePriorityException;
import com.orhanararat.exception.RuleBuildException;
import com.orhanararat.mapper.RuleMapper;
import com.orhanararat.model.IPAddressRange;
import com.orhanararat.model.RuleEntity;
import com.orhanararat.repository.RuleRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.drools.kiesession.rulebase.InternalKnowledgeBase;
import org.drools.kiesession.rulebase.KnowledgeBaseFactory;
import org.kie.api.definition.KiePackage;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RuleService {
    private final RuleRepository ruleRepository;
    private final RuleMapper ruleMapper;
    private final RuleHelperService ruleHelperService;
    private KieSession kieSession;

    private InternalKnowledgeBase internalKnowledgeBase = KnowledgeBaseFactory.newKnowledgeBase();

    @PostConstruct
    public void init() {
        loadRulesFromDatabase();
    }

    @Transactional
    public List<RuleResponseDto> getAllRules() {
        List<RuleEntity> ruleEntities = ruleHelperService.getAllRuleEntities();
        List<RuleResponseDto> ruleResponseList = new ArrayList<>();
        ruleEntities.forEach(ruleEntity -> ruleResponseList.add(ruleMapper.ruleResponseDtoToRuleEntity(ruleEntity)));
        return ruleResponseList;
    }

    @Transactional
    public RuleResponseDto addRule(RuleRequestDto requestDto) {
        int allByPriorityCount = ruleRepository.findAllByPriorityCount(requestDto.getPriority());
        if (allByPriorityCount != 0) {
            throw new NonUniquePriorityException("Cannot add rule same priority. priority: " + requestDto.getPriority());
        }
        RuleEntity ruleEntity = ruleMapper.ruleRequestDtoToRuleEntity(requestDto);
        RuleEntity savedRuleEntity = ruleRepository.save(ruleEntity);
        RuleResponseDto ruleResponseDto = ruleMapper.ruleResponseDtoToRuleEntity(savedRuleEntity);
        updateKieSession();
        return ruleResponseDto;
    }

    public void deleteRule(Long id) {
        ruleRepository.deleteById(id);
        updateKieSession();
    }

    private void loadRulesFromDatabase() {
        kieSession = internalKnowledgeBase.newKieSession();
        List<RuleEntity> ruleEntities = ruleHelperService.getAllRuleEntities();
        for (RuleEntity ruleEntity : ruleEntities) {
            addRuleToSession(ruleEntity);
        }
    }

    private void updateKieSession() {
        kieSession.dispose();
        ruleHelperService.evictAllRuleEntities();
        loadRulesFromDatabase();
    }

    private void addRuleToSession(RuleEntity ruleEntity) {
        String drl = generateDroolsRule(ruleEntity);
        KnowledgeBuilder knowledgeBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        knowledgeBuilder.add(ResourceFactory.newByteArrayResource(drl.getBytes()), ResourceType.DRL);
        if (knowledgeBuilder.hasErrors()) {
            throw new RuleBuildException("Rule build Exception: " + knowledgeBuilder.getErrors().toString());
        }
        Collection<KiePackage> packages = knowledgeBuilder.getKnowledgePackages();
        internalKnowledgeBase.addPackages(packages);

    }

    private String generateDroolsRule(RuleEntity ruleEntity) {
        return """
                package com.orhanararat.model;
                import com.orhanararat.model.IPAddressRange;
                rule "Rule_%d"
                    salience %d
                when
                    $fact : IPAddressRange()
                    eval(IPAddressRange.isInRange($fact.getStartIp(), "%s", "%s") &&
                            IPAddressRange.isInRange($fact.getEndIp(), "%s", "%s"))
                then
                    insert(new Boolean(%b));
                end
                """.formatted(
                ruleEntity.getId(),
                ruleEntity.getPriority(),
                ruleEntity.getSourceRange().getStartIp(), ruleEntity.getSourceRange().getEndIp(),
                ruleEntity.getDestinationRange().getStartIp(), ruleEntity.getDestinationRange().getEndIp(),
                ruleEntity.getAllow()
        );

    }

    public boolean evaluateIpAddresses(CheckRequestDto checkRequestDto) {
        IPAddressRange ipAddressRange = new IPAddressRange(checkRequestDto.getSourceIp(), checkRequestDto.getDestIp());
        kieSession.insert(ipAddressRange);
        kieSession.fireAllRules();

        boolean result = kieSession.getObjects(o -> o instanceof Boolean)
                .stream()
                .map(o -> (Boolean) o)
                .findFirst()
                .orElse(false);

        clearAllFacts(kieSession);

        return result;
    }

    public static void clearAllFacts(KieSession kieSession) {
        List<FactHandle> factHandles = new ArrayList<>(kieSession.getFactHandles());
        for (FactHandle factHandle : factHandles) {
            kieSession.delete(factHandle);
        }
    }
}
