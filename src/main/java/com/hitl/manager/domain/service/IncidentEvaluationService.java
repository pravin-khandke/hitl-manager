package com.hitl.manager.domain.service;

import com.hitl.manager.domain.model.AISuggestion;
import com.hitl.manager.domain.model.Incident;
import com.hitl.manager.domain.port.AIModel;

public class IncidentEvaluationService {
    private final AIModel model;

    public IncidentEvaluationService(AIModel model) { this.model = model; }

    public AISuggestion evaluate(Incident incident) {
        return model.predict(incident);
    }

    public AIModel getModel() { return model; }
}
