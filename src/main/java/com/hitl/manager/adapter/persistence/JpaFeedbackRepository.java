package com.hitl.manager.adapter.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

interface JpaFeedbackRepository extends JpaRepository<FeedbackRecordEntity, UUID> {
}
