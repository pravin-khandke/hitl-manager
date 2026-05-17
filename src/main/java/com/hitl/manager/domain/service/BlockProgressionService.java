package com.hitl.manager.domain.service;

import com.hitl.manager.domain.model.*;
import com.hitl.manager.domain.port.*;
import java.util.*;

public class BlockProgressionService {
    private final IncidentRepository incidentRepository;
    private final MetricsRepository metricsRepository;

    public BlockProgressionService(IncidentRepository incidentRepository,
                                    MetricsRepository metricsRepository) {
        this.incidentRepository = incidentRepository;
        this.metricsRepository = metricsRepository;
    }

    public boolean isBlockComplete(int blockNumber) {
        long total = incidentRepository.countByBlock(blockNumber);
        long processed = incidentRepository.findByBlock(blockNumber).stream()
                .filter(Incident::isProcessed).count();
        return total > 0 && processed >= total;
    }

    public int getCurrentBlock() {
        for (int block = 1; block <= 5; block++) {
            if (!isBlockComplete(block)) return block;
        }
        return 5;
    }

    public Optional<Incident> nextUnprocessed() {
        return incidentRepository.findUnprocessed().stream()
                .min(Comparator.comparing(Incident::getCreatedAt));
    }

    public boolean isAllComplete() {
        return incidentRepository.findUnprocessed().isEmpty();
    }
}
