package com.hitl.manager.adapter.web.dto;

import java.util.Map;
import java.util.UUID;

public record SimulatorStateDto(
    UUID incidentId,
    String incidentDescription,
    String incidentType,
    String severity,
    UUID suggestionId,
    String suggestedAction,
    double confidenceScore,
    String explanation,
    int currentBlock,
    int totalIncidents,
    int processedCount,
    Map<String, Double> resourceMetrics
) {}
