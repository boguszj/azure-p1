package com.azurep1.p1be.report.application;

import com.azurep1.p1be.limitation.domain.LimitationRepository;
import com.azurep1.p1be.report.domain.ReportRepository;
import com.azurep1.p1be.report.domain.UsageFragment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsageCalculator {

    private final ReportRepository reportRepository;
    private final LimitationRepository limitationRepository;
    private final Clock clock;

    public Map<String, UsageFragment> calculate() {
        var limitations = limitationRepository.findAll();
        return limitations.stream().map(limitation -> {
            var used = reportRepository.getUsageForDomain(limitation.getDomain(), LocalDateTime.now(clock).minusSeconds(limitation.getPeriodSeconds()));
            return new UsageFragment(
                    limitation.getDomain(),
                    limitation.getPeriodSeconds(),
                    limitation.getLimitationSeconds(),
                    used
            );
        }).collect(Collectors.toMap(UsageFragment::getDomain, usageFragment -> usageFragment));
    }

}
