package com.hitl.manager.adapter.persistence;

import com.hitl.manager.domain.model.AISuggestion;
import com.hitl.manager.domain.model.FeedbackRecord;
import com.hitl.manager.domain.model.OperatorAction;
import com.hitl.manager.domain.port.FeedbackStore;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional
class PostgresFeedbackStore implements FeedbackStore {
    private final JpaSuggestionRepository suggestionRepo;
    private final JpaActionRepository actionRepo;
    private final JpaFeedbackRepository feedbackRepo;

    PostgresFeedbackStore(JpaSuggestionRepository suggestionRepo,
                          JpaActionRepository actionRepo,
                          JpaFeedbackRepository feedbackRepo) {
        this.suggestionRepo = suggestionRepo;
        this.actionRepo = actionRepo;
        this.feedbackRepo = feedbackRepo;
    }

    @Override
    public AISuggestion saveSuggestion(AISuggestion suggestion) {
        return suggestionRepo.save(AISuggestionEntity.from(suggestion)).toDomain();
    }

    @Override
    public OperatorAction saveAction(OperatorAction action) {
        return actionRepo.save(OperatorActionEntity.from(action)).toDomain();
    }

    @Override
    public FeedbackRecord saveFeedback(FeedbackRecord record) {
        return feedbackRepo.save(FeedbackRecordEntity.from(record)).toDomain();
    }

    @Override
    public List<OperatorAction> findActionsByIncident(UUID incidentId) {
        return actionRepo.findByIncidentId(incidentId).stream()
                .map(OperatorActionEntity::toDomain).toList();
    }

    @Override
    public List<OperatorAction> findActionsByBlock(int blockNumber) {
        return actionRepo.findByBlockNumber(blockNumber).stream()
                .map(OperatorActionEntity::toDomain).toList();
    }

    @Override
    public Optional<OperatorAction> findActionByIncident(UUID incidentId) {
        return actionRepo.findFirstByIncidentId(incidentId)
                .map(OperatorActionEntity::toDomain);
    }

    @Override
    public long countOverridesInBlock(int blockNumber) {
        return actionRepo.countOverridesByBlockNumber(blockNumber);
    }

    @Override
    public double averageResponseTimeInBlock(int blockNumber) {
        Double avg = actionRepo.averageResponseTimeByBlockNumber(blockNumber);
        return avg != null ? avg : 0.0;
    }
}
