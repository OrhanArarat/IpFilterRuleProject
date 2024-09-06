package com.orhanararat.controller;

import com.orhanararat.dto.request.CheckRequestDto;
import com.orhanararat.dto.request.RuleRequestDto;
import com.orhanararat.dto.response.RuleResponseDto;
import com.orhanararat.service.RuleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rules")
@RequiredArgsConstructor
public class RuleController {
    private final RuleService ruleService;

    @GetMapping
    public ResponseEntity<List<RuleResponseDto>> getAllRules() {
        return ResponseEntity.ok(ruleService.getAllRules());
    }

    @PostMapping
    public ResponseEntity<RuleResponseDto> addRule(@Valid @RequestBody RuleRequestDto ruleRequest) {
        return ResponseEntity.ok(ruleService.addRule(ruleRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRule(@PathVariable Long id) {
        ruleService.deleteRule(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/check")
    public ResponseEntity<Boolean> checkSourceAndDestIp(@RequestBody CheckRequestDto checkRequest) {
        return ResponseEntity.ok(ruleService.evaluateIpAddresses(checkRequest));
    }
}
