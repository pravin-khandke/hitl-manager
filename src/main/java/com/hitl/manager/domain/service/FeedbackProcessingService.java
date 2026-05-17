package com.hitl.manager.domain.service;

import com.hitl.manager.domain.model.*;
import com.hitl.manager.domain.port.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;

public class FeedbackProcessingService {
    private static final Logger log = LoggerFactory.getLogger(FeedbackProcessingService.class);
    private final AIModel model;
    private final FeedbackStore feedbackStore;
    private final IncidentRepository incidentRepository;

    public FeedbackProcessingService(AIModel model, FeedbackStore feedbackStore,
                                      IncidentRepository incidentRepository) {
        this.model = model;
        this.feedbackStore = feedbackStore;
        this.incidentRepository = incidentRepository;
    }

    public FeedbackRecord processFeedback(OperatorAction action) {
        boolean modelUpdated = false;
        Map<String, Object> parameterDelta = new HashMap<>();

        if (action.isOverride()) {
            incidentRepository.findById(action.getIncidentId()).ifPresent(incident -> {
                model.learn(incident, action);
            });
            modelUpdated = true;
            parameterDelta.put("accuracy", model.getAccuracy());
            log.info("Model updated from operator override on incident {}", action.getIncidentId());
        }

        FeedbackRecord record = new FeedbackRecord(UUID.randomUUID(), action.getId(),
                modelUpdated, parameterDelta);
        return feedbackStore.saveFeedback(record);
    }
}
