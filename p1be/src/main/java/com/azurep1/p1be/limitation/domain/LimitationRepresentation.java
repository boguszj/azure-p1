package com.azurep1.p1be.limitation.domain;

import lombok.*;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class LimitationRepresentation {

    private LimitationId id;
    private Long limitationSeconds;
    private Long periodSeconds;
    private String domain;

}
