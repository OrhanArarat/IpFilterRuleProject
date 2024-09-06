package com.orhanararat.mapper;

import com.orhanararat.dto.request.RuleRequestDto;
import com.orhanararat.dto.response.RuleResponseDto;
import com.orhanararat.model.RuleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RuleMapper {
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    RuleEntity ruleRequestDtoToRuleEntity(RuleRequestDto ruleRequestDto);

    RuleResponseDto ruleResponseDtoToRuleEntity(RuleEntity ruleEntity);
}
