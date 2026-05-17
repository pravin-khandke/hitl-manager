package com.hitl.manager.domain.service;

import com.hitl.manager.domain.model.*;
import com.hitl.manager.domain.port.FeedbackStore;
import java.util.UUID;

public class OperatorInteractionService {
    private final FeedbackStore feedbackStore;

    public OperatorInteractionService(FeedbackStore feedbackStore) {
        this.feedbackStore = feedbackStore;
    }

    public OperatorAction recordAction(UUID suggestionId, UUID incidentId,
                                        Decision decision, String modification,
                                        int responseTimeMs, String operatorId) {
        OperatorAction action = new OperatorAction(UUID.randomUUID(), suggestionId,
                incidentId, decision, modification, responseTimeMs, operatorId);
        return feedbackStore.saveAction(action);
    }
}
