package com.azurep1.p1be.report.application;

import com.azurep1.p1be.report.domain.Report;

import java.util.List;

public interface ReportArchiveStorage {

    void archive(List<Report> reports);

}
