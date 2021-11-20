package com.azurep1.p1be.report.api;

import com.azurep1.p1be.report.application.ReportService;
import com.azurep1.p1be.report.domain.CreateReportDto;
import com.azurep1.p1be.report.domain.UsageFragment;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/report")
public class ReportController {

    private final ReportService service;

    @PostMapping
    public Map<String, UsageFragment> report(CreateReportDto dto) {
        return service.report(dto);
    }

}
