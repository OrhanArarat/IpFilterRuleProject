package com.orhanararat.controller;

import com.orhanararat.dto.request.CheckRequestDto;
import com.orhanararat.dto.request.RuleRequestDto;
import com.orhanararat.dto.response.RuleResponseDto;
import com.orhanararat.model.IPAddressRange;
import com.orhanararat.service.RuleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class RuleControllerTest {

    @Mock
    private RuleService ruleService;

    @InjectMocks
    private RuleController ruleController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllRules_ShouldReturnListOfRules() {
        // Given
        IPAddressRange sourceAddressRange1 = IPAddressRange.builder()
                .startIp("192.168.1.1")
                .endIp("192.168.1.20")
                .build();
        IPAddressRange destAddressRange1 = IPAddressRange.builder()
                .startIp("192.168.1.20")
                .endIp("192.168.1.30")
                .build();

        IPAddressRange sourceAddressRange2 = IPAddressRange.builder()
                .startIp("192.168.1.1")
                .endIp("192.168.1.40")
                .build();
        IPAddressRange destAddressRange2 = IPAddressRange.builder()
                .startIp("192.168.1.20")
                .endIp("192.168.1.50")
                .build();
        List<RuleResponseDto> expectedRules = Arrays.asList(
                new RuleResponseDto(1L, sourceAddressRange1, destAddressRange1, 1, true),
                new RuleResponseDto(2L, sourceAddressRange2, destAddressRange2, 2, true)
        );
        when(ruleService.getAllRules()).thenReturn(expectedRules);

        // When
        ResponseEntity<List<RuleResponseDto>> response = ruleController.getAllRules();

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedRules, response.getBody());
        verify(ruleService, times(1)).getAllRules();
    }

    @Test
    void addRule_ShouldReturnAddedRule() {
        // Given
        IPAddressRange sourceAddressRange1 = IPAddressRange.builder()
                .startIp("192.168.1.1")
                .endIp("192.168.1.20")
                .build();
        IPAddressRange destAddressRange1 = IPAddressRange.builder()
                .startIp("192.168.1.20")
                .endIp("192.168.1.30")
                .build();
        RuleRequestDto ruleRequest = new RuleRequestDto(sourceAddressRange1, destAddressRange1, 1L, true);
        RuleResponseDto expectedResponse = new RuleResponseDto(1L, sourceAddressRange1, destAddressRange1, 1, true);
        when(ruleService.addRule(ruleRequest)).thenReturn(expectedResponse);

        // When
        ResponseEntity<RuleResponseDto> response = ruleController.addRule(ruleRequest);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
        verify(ruleService, times(1)).addRule(ruleRequest);
    }

    @Test
    void deleteRule_ShouldReturnOkResponse() {
        // Given
        Long ruleId = 1L;

        // When
        ResponseEntity<Void> response = ruleController.deleteRule(ruleId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(ruleService, times(1)).deleteRule(ruleId);
    }

    @Test
    void checkSourceAndDestIp_ShouldReturnBooleanResult() {
        // Given
        CheckRequestDto checkRequest = new CheckRequestDto("192.168.1.1", "10.0.0.1");
        when(ruleService.evaluateIpAddresses(checkRequest)).thenReturn(true);

        // When
        ResponseEntity<Boolean> response = ruleController.checkSourceAndDestIp(checkRequest);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(Boolean.TRUE.equals(response.getBody()));
        verify(ruleService, times(1)).evaluateIpAddresses(checkRequest);
    }
}