package com.hitl.manager.adapter.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

interface JpaMetricsRepository extends JpaRepository<BlockMetricsEntity, UUID> {
    Optional<BlockMetricsEntity> findByBlockNumber(int blockNumber);
}
