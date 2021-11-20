package com.azurep1.p1be.report.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UsageFragment {

    private String domain;
    private Long periodSeconds;
    private Long limitationSeconds;
    private Long usedSeconds;

}
