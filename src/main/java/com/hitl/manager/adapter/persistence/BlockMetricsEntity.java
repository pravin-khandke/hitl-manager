package com.hitl.manager.adapter.persistence;

import com.hitl.manager.domain.model.IncidentBlock;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "block_metrics")
class BlockMetricsEntity {
    @Id
    private UUID id;
    @Column(name = "block_number", nullable = false, unique = true)
    private int blockNumber;
    @Column(name = "ai_accuracy", nullable = false)
    private double aiAccuracy;
    @Column(name = "human_override_rate", nullable = false)
    private double humanOverrideRate;
    @Column(name = "false_positive_rate", nullable = false)
    private double falsePositiveRate;
    @Column(name = "resolution_time_min", nullable = false)
    private double resolutionTimeMin;
    @Column(name = "computed_at", nullable = false)
    private Instant computedAt;

    BlockMetricsEntity() {}

    static BlockMetricsEntity from(IncidentBlock b) {
        BlockMetricsEntity e = new BlockMetricsEntity();
        e.id = UUID.randomUUID();
        e.blockNumber = b.getBlockNumber();
        e.aiAccuracy = b.getAiAccuracy();
        e.humanOverrideRate = b.getHumanOverrideRate();
        e.falsePositiveRate = b.getFalsePositiveRate();
        e.resolutionTimeMin = b.getResolutionTimeMin();
        e.computedAt = Instant.now();
        return e;
    }

    IncidentBlock toDomain() {
        return new IncidentBlock(blockNumber, aiAccuracy, humanOverrideRate,
                falsePositiveRate, resolutionTimeMin, 0, 0);
    }
}
