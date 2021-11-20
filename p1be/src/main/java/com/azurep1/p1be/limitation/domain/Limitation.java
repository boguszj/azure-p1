package com.azurep1.p1be.limitation.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Entity
@Table(name = "limitation")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Limitation {

    @Id
    @NonNull
    private LimitationId id;

    @Column(name = "limitation_seconds")
    private Long limitationSeconds;

    @Column(name = "period_seconds")
    private Long periodSeconds;

    @Column(name = "domain")
    private String domain;

    public Limitation(Long limitationSeconds, Long periodSeconds, String domain) {
        this.id = LimitationId.newId();
        this.domain = domain;
        update(limitationSeconds, periodSeconds);
    }

    public void update(Long limitationSeconds, Long periodSeconds) {
        this.limitationSeconds = limitationSeconds;
        this.periodSeconds = periodSeconds;
    }
}

