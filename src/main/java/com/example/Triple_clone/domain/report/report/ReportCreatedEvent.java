package com.example.Triple_clone.domain.report.report;

import com.example.Triple_clone.domain.report.Report;

public record ReportCreatedEvent(Report report, long reportCount) {}

