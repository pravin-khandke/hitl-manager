package com.hitl.manager.domain.model;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Incident {
    private final UUID id;
    private final int blockNumber;
    private final SeverityLevel severity;
    private final String type;
    private final String description;
    private final Map<String, Double> resourceMetrics;
    private final String sourceIp;
    private final String destinationIp;
    private final boolean groundTruth;
    private final List<String> resolutionTags;
    private final Instant createdAt;
    private boolean processed;

    public Incident(UUID id, int blockNumber, SeverityLevel severity, String type,
                    String description, Map<String, Double> resourceMetrics,
                    String sourceIp, String destinationIp, boolean groundTruth,
                    List<String> resolutionTags, Instant createdAt) {
        this.id = id;
        this.blockNumber = blockNumber;
        this.severity = severity;
        this.type = type;
        this.description = description;
        this.resourceMetrics = resourceMetrics;
        this.sourceIp = sourceIp;
        this.destinationIp = destinationIp;
        this.groundTruth = groundTruth;
        this.resolutionTags = resolutionTags;
        this.createdAt = createdAt;
        this.processed = false;
    }

    public UUID getId() { return id; }
    public int getBlockNumber() { return blockNumber; }
    public SeverityLevel getSeverity() { return severity; }
    public String getType() { return type; }
    public String getDescription() { return description; }
    public Map<String, Double> getResourceMetrics() { return resourceMetrics; }
    public String getSourceIp() { return sourceIp; }
    public String getDestinationIp() { return destinationIp; }
    public boolean isGroundTruth() { return groundTruth; }
    public List<String> getResolutionTags() { return resolutionTags; }
    public Instant getCreatedAt() { return createdAt; }
    public boolean isProcessed() { return processed; }
    public void markProcessed() { this.processed = true; }
}
