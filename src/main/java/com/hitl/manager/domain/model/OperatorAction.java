package com.hitl.manager.domain.model;

import java.util.UUID;

public class OperatorAction {
    private final UUID id;
    private final UUID suggestionId;
    private final UUID incidentId;
    private final Decision decision;
    private final String modification;
    private final int responseTimeMs;
    private final String operatorId;

    public OperatorAction(UUID id, UUID suggestionId, UUID incidentId, Decision decision,
                          String modification, int responseTimeMs, String operatorId) {
        this.id = id;
        this.suggestionId = suggestionId;
        this.incidentId = incidentId;
        this.decision = decision;
        this.modification = modification;
        this.responseTimeMs = responseTimeMs;
        this.operatorId = operatorId;
    }

    public UUID getId() { return id; }
    public UUID getSuggestionId() { return suggestionId; }
    public UUID getIncidentId() { return incidentId; }
    public Decision getDecision() { return decision; }
    public String getModification() { return modification; }
    public int getResponseTimeMs() { return responseTimeMs; }
    public String getOperatorId() { return operatorId; }
    public boolean isOverride() { return decision == Decision.MODIFY || decision == Decision.REJECT; }
}
