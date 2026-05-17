package com.hitl.manager.adapter.persistence;

import com.hitl.manager.domain.model.FeedbackRecord;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.*;

@Entity
@Table(name = "feedback_records")
class FeedbackRecordEntity {
    @Id
    private UUID id;
    @Column(name = "action_id", nullable = false)
    private UUID actionId;
    @Column(name = "model_updated", nullable = false)
    private boolean modelUpdated;
    @Column(name = "parameter_delta", length = 2000)
    private String parameterDeltaJson;
    @Column(name = "learned_at", nullable = false)
    private Instant learnedAt;

    FeedbackRecordEntity() {}

    static FeedbackRecordEntity from(FeedbackRecord r) {
        FeedbackRecordEntity e = new FeedbackRecordEntity();
        e.id = r.getId();
        e.actionId = r.getActionId();
        e.modelUpdated = r.isModelUpdated();
        e.parameterDeltaJson = mapToJson(r.getParameterDelta());
        e.learnedAt = Instant.now();
        return e;
    }

    FeedbackRecord toDomain() {
        return new FeedbackRecord(id, actionId, modelUpdated, jsonToMap(parameterDeltaJson));
    }

    private static String mapToJson(Map<String, Object> map) {
        if (map == null || map.isEmpty()) return "{}";
        StringBuilder sb = new StringBuilder("{");
        for (var entry : map.entrySet()) {
            if (sb.length() > 1) sb.append(",");
            sb.append("\"").append(entry.getKey()).append("\":").append(entry.getValue());
        }
        sb.append("}");
        return sb.toString();
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> jsonToMap(String json) {
        if (json == null || json.equals("{}")) return Map.of();
        Map<String, Object> map = new HashMap<>();
        String inner = json.substring(1, json.length() - 1);
        if (inner.isEmpty()) return map;
        for (String pair : inner.split(",")) {
            String[] kv = pair.split(":", 2);
            String key = kv[0].replace("\"", "").trim();
            String v = kv[1].trim();
            if (v.contains(".")) map.put(key, Double.parseDouble(v));
            else map.put(key, Integer.parseInt(v));
        }
        return map;
    }
}
