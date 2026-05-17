package com.hitl.manager.domain.model;

import java.util.Map;
import java.util.UUID;

public class FeedbackRecord {
    private final UUID id;
    private final UUID actionId;
    private final boolean modelUpdated;
    private final Map<String, Object> parameterDelta;

    public FeedbackRecord(UUID id, UUID actionId, boolean modelUpdated,
                          Map<String, Object> parameterDelta) {
        this.id = id;
        this.actionId = actionId;
        this.modelUpdated = modelUpdated;
        this.parameterDelta = parameterDelta;
    }

    public UUID getId() { return id; }
    public UUID getActionId() { return actionId; }
    public boolean isModelUpdated() { return modelUpdated; }
    public Map<String, Object> getParameterDelta() { return parameterDelta; }
}
