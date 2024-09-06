package com.orhanararat.dto.request;

import com.orhanararat.model.IPAddressRange;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RuleRequestDto {
    @NotNull(message = "Source range cannot be null")
    private IPAddressRange sourceRange;

    @NotNull(message = "Destination range cannot be null")
    private IPAddressRange destinationRange;

    @NotNull(message = "Priority cannot be null")
    @Min(value = 1, message = "Priority must be at least 1")
    @Max(value = 1000, message = "Priority must be at most 1000")
    private Long priority;

    @NotNull(message = "Allow flag cannot be null")
    private Boolean allow;
}
