package com.hitl.manager.domain.model;

import java.util.UUID;

public class AISuggestion {
    private final UUID id;
    private final UUID incidentId;
    private final String suggestedAction;
    private final double confidenceScore;
    private final String explanation;
    private final String modelVersion;

    public AISuggestion(UUID id, UUID incidentId, String suggestedAction,
                        double confidenceScore, String explanation, String modelVersion) {
        this.id = id;
        this.incidentId = incidentId;
        this.suggestedAction = suggestedAction;
        this.confidenceScore = confidenceScore;
        this.explanation = explanation;
        this.modelVersion = modelVersion;
    }

    public UUID getId() { return id; }
    public UUID getIncidentId() { return incidentId; }
    public String getSuggestedAction() { return suggestedAction; }
    public double getConfidenceScore() { return confidenceScore; }
    public String getExplanation() { return explanation; }
    public String getModelVersion() { return modelVersion; }
}
