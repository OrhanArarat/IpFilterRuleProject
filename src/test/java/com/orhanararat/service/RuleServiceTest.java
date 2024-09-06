package com.orhanararat.service;

import com.orhanararat.dto.request.CheckRequestDto;
import com.orhanararat.dto.request.RuleRequestDto;
import com.orhanararat.dto.response.RuleResponseDto;
import com.orhanararat.exception.NonUniquePriorityException;
import com.orhanararat.mapper.RuleMapper;
import com.orhanararat.model.IPAddressRange;
import com.orhanararat.model.RuleEntity;
import com.orhanararat.repository.RuleRepository;
import org.drools.kiesession.rulebase.InternalKnowledgeBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kie.api.runtime.KieSession;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RuleServiceTest {

    @InjectMocks
    private RuleService ruleService;

    @Mock
    private RuleRepository ruleRepository;

    @Spy
    private final RuleMapper ruleMapper = Mappers.getMapper(RuleMapper.class);

    @Mock
    private RuleHelperService ruleHelperService;

    @Mock
    private KieSession kieSession;

    @Mock
    private InternalKnowledgeBase internalKnowledgeBase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ruleService.setKieSession(kieSession);
    }

    @Test
    void getAllRules_shouldReturnListOfRuleResponseDto() {
        RuleEntity rule1 = new RuleEntity();
        RuleEntity rule2 = new RuleEntity();
        List<RuleEntity> ruleEntities = Arrays.asList(rule1, rule2);

        RuleResponseDto responseDto1 = new RuleResponseDto();
        RuleResponseDto responseDto2 = new RuleResponseDto();

        when(ruleHelperService.getAllRuleEntities()).thenReturn(ruleEntities);
        when(ruleMapper.ruleResponseDtoToRuleEntity(rule1)).thenReturn(responseDto1);
        when(ruleMapper.ruleResponseDtoToRuleEntity(rule2)).thenReturn(responseDto2);

        List<RuleResponseDto> result = ruleService.getAllRules();

        assertEquals(2, result.size());
        assertTrue(result.contains(responseDto1));
        assertTrue(result.contains(responseDto2));
        verify(ruleHelperService).getAllRuleEntities();
        verify(ruleMapper, times(2)).ruleResponseDtoToRuleEntity(any(RuleEntity.class));
    }

    @Test
    void addRule_shouldAddRuleAndReturnResponseDto() {
        RuleRequestDto requestDto = new RuleRequestDto();
        requestDto.setPriority(1L);

        RuleEntity ruleEntity = new RuleEntity();
        RuleEntity savedRuleEntity = new RuleEntity();
        RuleResponseDto responseDto = new RuleResponseDto();

        when(ruleRepository.findAllByPriorityCount(1L)).thenReturn(0);
        when(ruleMapper.ruleRequestDtoToRuleEntity(requestDto)).thenReturn(ruleEntity);
        when(ruleRepository.save(ruleEntity)).thenReturn(savedRuleEntity);
        when(ruleMapper.ruleResponseDtoToRuleEntity(savedRuleEntity)).thenReturn(responseDto);

        RuleResponseDto result = ruleService.addRule(requestDto);

        assertNotNull(result);
        assertEquals(responseDto, result);
        verify(ruleRepository).findAllByPriorityCount(1L);
        verify(ruleMapper).ruleRequestDtoToRuleEntity(requestDto);
        verify(ruleRepository).save(ruleEntity);
        verify(ruleMapper).ruleResponseDtoToRuleEntity(savedRuleEntity);
    }

    @Test
    void addRule_shouldThrowExceptionWhenPriorityNotUnique() {
        RuleRequestDto requestDto = new RuleRequestDto();
        requestDto.setPriority(1L);

        when(ruleRepository.findAllByPriorityCount(1L)).thenReturn(1);

        assertThrows(NonUniquePriorityException.class, () -> ruleService.addRule(requestDto));
        verify(ruleRepository).findAllByPriorityCount(1L);
    }

    @Test
    void deleteRule_shouldDeleteRuleAndUpdateKieSession() {
        Long ruleId = 1L;

        ruleService.deleteRule(ruleId);

        verify(ruleRepository).deleteById(ruleId);
    }

    @Test
    void evaluateIpAddresses_shouldReturnExpectedResult() {
        CheckRequestDto checkRequestDto = new CheckRequestDto();
        checkRequestDto.setSourceIp("192.168.1.1");
        checkRequestDto.setDestIp("10.0.0.1");

        when(kieSession.getObjects(any())).thenAnswer(invocation -> Collections.singletonList(Boolean.TRUE));

        boolean result = ruleService.evaluateIpAddresses(checkRequestDto);

        assertTrue(result);
        verify(kieSession).insert(any(IPAddressRange.class));
        verify(kieSession).fireAllRules();
        verify(kieSession).getObjects(any());
    }
}