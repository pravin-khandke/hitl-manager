package com.hitl.manager.domain.port;

import com.hitl.manager.domain.model.IncidentBlock;

import java.util.List;
import java.util.Optional;

public interface MetricsRepository {
    IncidentBlock save(IncidentBlock block);
    Optional<IncidentBlock> findByBlockNumber(int blockNumber);
    List<IncidentBlock> findAll();
    void deleteAll();
}
