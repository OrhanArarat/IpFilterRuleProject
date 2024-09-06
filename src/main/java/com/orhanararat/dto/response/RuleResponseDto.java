package com.orhanararat.dto.response;

import com.orhanararat.model.IPAddressRange;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RuleResponseDto {
    private Long id;
    private IPAddressRange sourceRange;
    private IPAddressRange destinationRange;
    private int priority;
    private boolean allow;
}
