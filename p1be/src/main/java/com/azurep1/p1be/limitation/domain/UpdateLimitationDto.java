package com.azurep1.p1be.limitation.domain;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@PeriodGreaterThanLimitation
public class UpdateLimitationDto implements PeriodAndLimitationDto {

    @With
    private LimitationId id;

    @NotNull
    @Min(0)
    private Long periodSeconds;

    @NotNull
    @Min(0)
    private Long limitationSeconds;

}
