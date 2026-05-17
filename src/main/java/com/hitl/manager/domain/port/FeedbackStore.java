package com.hitl.manager.domain.port;

import com.hitl.manager.domain.model.AISuggestion;
import com.hitl.manager.domain.model.FeedbackRecord;
import com.hitl.manager.domain.model.OperatorAction;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FeedbackStore {
    AISuggestion saveSuggestion(AISuggestion suggestion);
    OperatorAction saveAction(OperatorAction action);
    FeedbackRecord saveFeedback(FeedbackRecord record);
    List<OperatorAction> findActionsByIncident(UUID incidentId);
    List<OperatorAction> findActionsByBlock(int blockNumber);
    Optional<OperatorAction> findActionByIncident(UUID incidentId);
    long countOverridesInBlock(int blockNumber);
    double averageResponseTimeInBlock(int blockNumber);
}
