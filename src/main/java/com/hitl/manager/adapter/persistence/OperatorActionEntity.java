package com.hitl.manager.adapter.persistence;

import com.hitl.manager.domain.model.Decision;
import com.hitl.manager.domain.model.OperatorAction;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "operator_actions")
class OperatorActionEntity {
    @Id
    private UUID id;
    @Column(name = "suggestion_id", nullable = false)
    private UUID suggestionId;
    @Column(name = "incident_id", nullable = false)
    private UUID incidentId;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Decision decision;
    @Column(columnDefinition = "TEXT")
    private String modification;
    @Column(name = "response_time_ms", nullable = false)
    private int responseTimeMs;
    @Column(name = "operator_id", nullable = false)
    private String operatorId;
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    OperatorActionEntity() {}

    static OperatorActionEntity from(OperatorAction a) {
        OperatorActionEntity e = new OperatorActionEntity();
        e.id = a.getId();
        e.suggestionId = a.getSuggestionId();
        e.incidentId = a.getIncidentId();
        e.decision = a.getDecision();
        e.modification = a.getModification();
        e.responseTimeMs = a.getResponseTimeMs();
        e.operatorId = a.getOperatorId();
        e.createdAt = Instant.now();
        return e;
    }

    OperatorAction toDomain() {
        return new OperatorAction(id, suggestionId, incidentId, decision, modification,
                responseTimeMs, operatorId);
    }
}
