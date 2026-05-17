package com.hitl.manager.adapter.persistence;

import com.hitl.manager.domain.model.AISuggestion;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "ai_suggestions")
class AISuggestionEntity {
    @Id
    private UUID id;
    @Column(name = "incident_id", nullable = false)
    private UUID incidentId;
    @Column(name = "suggested_action", nullable = false)
    private String suggestedAction;
    @Column(name = "confidence_score", nullable = false)
    private double confidenceScore;
    @Column(columnDefinition = "TEXT")
    private String explanation;
    @Column(name = "model_version", nullable = false)
    private String modelVersion;
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    AISuggestionEntity() {}

    static AISuggestionEntity from(AISuggestion s) {
        AISuggestionEntity e = new AISuggestionEntity();
        e.id = s.getId();
        e.incidentId = s.getIncidentId();
        e.suggestedAction = s.getSuggestedAction();
        e.confidenceScore = s.getConfidenceScore();
        e.explanation = s.getExplanation();
        e.modelVersion = s.getModelVersion();
        e.createdAt = Instant.now();
        return e;
    }

    AISuggestion toDomain() {
        return new AISuggestion(id, incidentId, suggestedAction, confidenceScore, explanation, modelVersion);
    }
}
