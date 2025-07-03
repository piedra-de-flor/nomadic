package com.example.Triple_clone.domain.report.web.dto;

import com.example.Triple_clone.domain.report.domain.Report;

public record ReportCreatedEvent(Report report, long reportCount) {}

