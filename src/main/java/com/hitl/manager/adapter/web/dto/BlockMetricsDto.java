package com.hitl.manager.adapter.web.dto;

public record BlockMetricsDto(
    int blockNumber,
    double aiAccuracy,
    double humanOverrideRate,
    double falsePositiveRate,
    double resolutionTimeMin,
    int totalIncidents,
    int processedCount
) {}
