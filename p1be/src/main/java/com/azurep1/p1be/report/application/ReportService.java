package com.azurep1.p1be.report.application;

import com.azurep1.p1be.report.domain.CreateReportDto;
import com.azurep1.p1be.report.domain.Report;
import com.azurep1.p1be.report.domain.ReportRepository;
import com.azurep1.p1be.report.domain.UsageFragment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository repository;
    private final UsageCalculator calculator;
    private final Clock clock;

    public Map<String, UsageFragment> report(CreateReportDto dto) {
        var report = new Report(dto.getUrl(), dto.getIntervalSeconds(), clock);
        repository.save(report);
        return calculator.calculate();
    }

}
