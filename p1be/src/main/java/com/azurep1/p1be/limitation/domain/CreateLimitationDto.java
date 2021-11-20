package com.azurep1.p1be.limitation.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@PeriodGreaterThanLimitation
public class CreateLimitationDto implements PeriodAndLimitationDto {

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9][a-zA-Z0-9-]{1,61}[a-zA-Z0-9]\\.[a-zA-Z]{2,6}$")
    private String domain;

    @NotNull
    @Min(0)
    private Long periodSeconds;

    @NotNull
    @Min(0)
    private Long limitationSeconds;

}
