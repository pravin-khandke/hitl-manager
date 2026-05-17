package com.hitl.manager.adapter.persistence;

import com.hitl.manager.domain.model.SeverityLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

interface JpaIncidentRepository extends JpaRepository<IncidentEntity, UUID> {
    List<IncidentEntity> findByBlockNumber(int blockNumber);
    List<IncidentEntity> findBySeverity(SeverityLevel severity);
    List<IncidentEntity> findByProcessedFalse();
    long countByBlockNumber(int blockNumber);
}
