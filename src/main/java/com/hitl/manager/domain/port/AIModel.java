package com.hitl.manager.domain.port;

import com.hitl.manager.domain.model.AISuggestion;
import com.hitl.manager.domain.model.Incident;
import com.hitl.manager.domain.model.ModelMetadata;
import com.hitl.manager.domain.model.OperatorAction;

public interface AIModel {
    AISuggestion predict(Incident incident);
    void learn(Incident incident, OperatorAction action);
    double getAccuracy();
    double getConfidence(Incident incident);
    ModelMetadata getMetadata();
    void reset();
}
