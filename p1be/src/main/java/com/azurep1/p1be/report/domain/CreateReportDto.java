package com.azurep1.p1be.report.domain;

import com.azurep1.p1be.limitation.domain.PeriodGreaterThanLimitation;
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
public class CreateReportDto {

    @NotBlank
    @Pattern(regexp = "https?://(www\\.)?[-a-zA-Z0-9@:%._+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_+.~#?&/=]*)")
    private String url;

    @NotNull
    @Min(5)
    @Min(60)
    private Long intervalSeconds;

}
