package com.hitl.manager.adapter.persistence;

import com.hitl.manager.domain.model.Incident;
import com.hitl.manager.domain.model.SeverityLevel;
import com.hitl.manager.domain.port.IncidentRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional
class PostgresIncidentRepository implements IncidentRepository {
    private final JpaIncidentRepository jpa;

    PostgresIncidentRepository(JpaIncidentRepository jpa) { this.jpa = jpa; }

    @Override
    public List<Incident> findAll() {
        return jpa.findAll().stream().map(IncidentEntity::toDomain).toList();
    }

    @Override
    public List<Incident> findByBlock(int blockNumber) {
        return jpa.findByBlockNumber(blockNumber).stream().map(IncidentEntity::toDomain).toList();
    }

    @Override
    public List<Incident> findBySeverity(SeverityLevel severity) {
        return jpa.findBySeverity(severity).stream().map(IncidentEntity::toDomain).toList();
    }

    @Override
    public List<Incident> findUnprocessed() {
        return jpa.findByProcessedFalse().stream().map(IncidentEntity::toDomain).toList();
    }

    @Override
    public Optional<Incident> findById(UUID id) {
        return jpa.findById(id).map(IncidentEntity::toDomain);
    }

    @Override
    public Incident save(Incident incident) {
        return jpa.save(IncidentEntity.from(incident)).toDomain();
    }

    @Override
    public void saveAll(List<Incident> incidents) {
        jpa.saveAll(incidents.stream().map(IncidentEntity::from).toList());
    }

    @Override public long count() { return jpa.count(); }
    @Override public long countByBlock(int blockNumber) { return jpa.countByBlockNumber(blockNumber); }
}
