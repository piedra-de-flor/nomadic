package com.example.Triple_clone.dto.report;

import com.example.Triple_clone.domain.entity.Report;

public record ReportCreatedEvent(Report report, long reportCount) {}

