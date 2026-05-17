package com.hitl.manager.adapter.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

interface JpaSuggestionRepository extends JpaRepository<AISuggestionEntity, UUID> {
    List<AISuggestionEntity> findByIncidentId(UUID incidentId);
}
