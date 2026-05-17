package com.hitl.manager.adapter.persistence;

import com.hitl.manager.domain.model.Incident;
import com.hitl.manager.domain.model.SeverityLevel;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.*;

@Entity
@Table(name = "incidents")
class IncidentEntity {
    @Id
    private UUID id;
    @Column(name = "block_number", nullable = false)
    private int blockNumber;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeverityLevel severity;
    @Column(nullable = false)
    private String type;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;
    @Column(name = "resource_metrics", length = 4000)
    private String resourceMetricsJson;
    @Column(name = "source_ip")
    private String sourceIp;
    @Column(name = "destination_ip")
    private String destinationIp;
    @Column(name = "ground_truth", nullable = false)
    private boolean groundTruth;
    @Column(name = "resolution_tags", length = 1000)
    private String resolutionTagsCsv;
    @Column(nullable = false)
    private Instant createdAt;
    @Column(nullable = false)
    private boolean processed;

    IncidentEntity() {}

    static IncidentEntity from(Incident i) {
        IncidentEntity e = new IncidentEntity();
        e.id = i.getId();
        e.blockNumber = i.getBlockNumber();
        e.severity = i.getSeverity();
        e.type = i.getType();
        e.description = i.getDescription();
        e.resourceMetricsJson = mapToJson(i.getResourceMetrics());
        e.sourceIp = i.getSourceIp();
        e.destinationIp = i.getDestinationIp();
        e.groundTruth = i.isGroundTruth();
        e.resolutionTagsCsv = i.getResolutionTags() != null ? String.join(",", i.getResolutionTags()) : null;
        e.createdAt = i.getCreatedAt();
        e.processed = i.isProcessed();
        return e;
    }

    Incident toDomain() {
        Incident i = new Incident(id, blockNumber, severity, type, description,
                jsonToMap(resourceMetricsJson), sourceIp, destinationIp, groundTruth,
                resolutionTagsCsv != null && !resolutionTagsCsv.isEmpty()
                        ? List.of(resolutionTagsCsv.split(",")) : List.of(),
                createdAt);
        if (processed) i.markProcessed();
        return i;
    }

    private static String mapToJson(Map<String, Double> map) {
        if (map == null || map.isEmpty()) return "{}";
        StringBuilder sb = new StringBuilder("{");
        for (var entry : map.entrySet()) {
            if (sb.length() > 1) sb.append(",");
            sb.append("\"").append(entry.getKey()).append("\":").append(entry.getValue());
        }
        sb.append("}");
        return sb.toString();
    }

    private static Map<String, Double> jsonToMap(String json) {
        if (json == null || json.equals("{}")) return Map.of();
        Map<String, Double> map = new HashMap<>();
        String inner = json.substring(1, json.length() - 1);
        if (inner.isEmpty()) return map;
        for (String pair : inner.split(",")) {
            String[] kv = pair.split(":", 2);
            String key = kv[0].replace("\"", "").trim();
            double val = Double.parseDouble(kv[1].trim());
            map.put(key, val);
        }
        return map;
    }
}
