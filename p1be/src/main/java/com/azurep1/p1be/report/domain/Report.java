package com.azurep1.p1be.report.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Clock;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "report")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Report {

    @Id
    private ReportId id;

    @Column(name = "url")
    private String url;

    @Column(name = "reported_at")
    private LocalDateTime reportedAt;

    @Column(name = "report_interval_seconds")
    private Long reportIntervalSeconds;

    public Report(String url, Long reportIntervalSeconds, Clock clock) {
        this.id = ReportId.newId();
        this.url = url;
        this.reportedAt = LocalDateTime.now(clock);
        this.reportIntervalSeconds = reportIntervalSeconds;
    }

}
