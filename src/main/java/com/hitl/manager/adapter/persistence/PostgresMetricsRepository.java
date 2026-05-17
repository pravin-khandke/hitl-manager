package com.hitl.manager.adapter.persistence;

import com.hitl.manager.domain.model.IncidentBlock;
import com.hitl.manager.domain.port.MetricsRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
class PostgresMetricsRepository implements MetricsRepository {
    private final JpaMetricsRepository jpa;

    PostgresMetricsRepository(JpaMetricsRepository jpa) { this.jpa = jpa; }

    @Override
    public IncidentBlock save(IncidentBlock block) {
        jpa.findByBlockNumber(block.getBlockNumber()).ifPresent(existing ->
                jpa.delete(existing));
        return jpa.save(BlockMetricsEntity.from(block)).toDomain();
    }

    @Override
    public Optional<IncidentBlock> findByBlockNumber(int blockNumber) {
        return jpa.findByBlockNumber(blockNumber).map(BlockMetricsEntity::toDomain);
    }

    @Override
    public List<IncidentBlock> findAll() {
        return jpa.findAll().stream().map(BlockMetricsEntity::toDomain).toList();
    }

    @Override
    public void deleteAll() { jpa.deleteAll(); }
}
