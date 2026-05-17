package com.hitl.manager.domain.port;

import com.hitl.manager.domain.model.Incident;
import com.hitl.manager.domain.model.SeverityLevel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IncidentRepository {
    List<Incident> findAll();
    List<Incident> findByBlock(int blockNumber);
    List<Incident> findBySeverity(SeverityLevel severity);
    List<Incident> findUnprocessed();
    Optional<Incident> findById(UUID id);
    Incident save(Incident incident);
    void saveAll(List<Incident> incidents);
    long count();
    long countByBlock(int blockNumber);
}
