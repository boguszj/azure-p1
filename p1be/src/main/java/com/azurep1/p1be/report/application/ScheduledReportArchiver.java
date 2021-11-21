package com.azurep1.p1be.report.application;

import com.azurep1.p1be.report.domain.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class ScheduledReportArchiver {

    @Value("${app.archivization.report-age-threshold}")
    private Duration reportAgeArchivizationThreshold;

    private final ReportArchiveStorage storage;
    private final ReportRepository repository;
    private final Clock clock;

    @Scheduled(fixedDelayString = "${app.archivization.interval-ms}")
    public void archiveReports() {
        var toArchive = repository.findAllForArchivization(LocalDateTime.now(clock).minus(reportAgeArchivizationThreshold));
        if (toArchive.size() > 0) {
            storage.archive(toArchive);
            repository.deleteAll(toArchive);
        }
    }

}
