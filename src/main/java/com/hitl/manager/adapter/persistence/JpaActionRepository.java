package com.hitl.manager.adapter.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

interface JpaActionRepository extends JpaRepository<OperatorActionEntity, UUID> {
    List<OperatorActionEntity> findByIncidentId(UUID incidentId);
    Optional<OperatorActionEntity> findFirstByIncidentId(UUID incidentId);

    @Query("SELECT a FROM OperatorActionEntity a WHERE a.incidentId IN " +
           "(SELECT i.id FROM IncidentEntity i WHERE i.blockNumber = :blockNumber)")
    List<OperatorActionEntity> findByBlockNumber(int blockNumber);

    @Query("SELECT COUNT(a) FROM OperatorActionEntity a WHERE a.incidentId IN " +
           "(SELECT i.id FROM IncidentEntity i WHERE i.blockNumber = :blockNumber) " +
           "AND a.decision IN ('MODIFY', 'REJECT')")
    long countOverridesByBlockNumber(int blockNumber);

    @Query("SELECT AVG(a.responseTimeMs) FROM OperatorActionEntity a WHERE a.incidentId IN " +
           "(SELECT i.id FROM IncidentEntity i WHERE i.blockNumber = :blockNumber)")
    Double averageResponseTimeByBlockNumber(int blockNumber);
}
