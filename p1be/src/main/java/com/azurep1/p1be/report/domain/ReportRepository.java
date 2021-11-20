package com.azurep1.p1be.report.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ReportRepository extends JpaRepository<Report, ReportId> {

    @Query("select sum(r.reportIntervalSeconds) from Report r where r.reportedAt < :periodStart and r.url like CONCAT('%', :domain, '%')")
    Long getUsageForDomain(String domain, LocalDateTime periodStart);

    @Query("select r from Report r where r.reportedAt < :threshold")
    List<Report> findAllForArchivization(LocalDateTime threshold);

}
