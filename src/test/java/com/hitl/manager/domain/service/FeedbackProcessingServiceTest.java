package com.hitl.manager.domain.service;

import com.hitl.manager.domain.model.*;
import com.hitl.manager.domain.port.*;
import org.junit.jupiter.api.*;
import java.time.Instant;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FeedbackProcessingServiceTest {
    private AIModel model;
    private FeedbackStore feedbackStore;
    private IncidentRepository incidentRepository;
    private FeedbackProcessingService service;

    @BeforeEach
    void setUp() {
        model = mock(AIModel.class);
        feedbackStore = mock(FeedbackStore.class);
        incidentRepository = mock(IncidentRepository.class);
        service = new FeedbackProcessingService(model, feedbackStore, incidentRepository);
    }

    @Test
    void triggersModelLearnOnOverride() {
        Incident incident = createIncident(false);
        OperatorAction action = createAction(Decision.MODIFY);
        when(incidentRepository.findById(action.getIncidentId())).thenReturn(Optional.of(incident));
        when(feedbackStore.saveFeedback(any())).thenAnswer(inv -> inv.getArgument(0));

        FeedbackRecord record = service.processFeedback(action);

        verify(model).learn(incident, action);
        assertTrue(record.isModelUpdated());
    }

    @Test
    void doesNotTriggerLearnOnAccept() {
        OperatorAction action = createAction(Decision.ACCEPT);
        when(feedbackStore.saveFeedback(any())).thenAnswer(inv -> inv.getArgument(0));

        FeedbackRecord record = service.processFeedback(action);

        verify(model, never()).learn(any(), any());
        assertFalse(record.isModelUpdated());
    }

    @Test
    void savesFeedbackRecord() {
        OperatorAction action = createAction(Decision.REJECT);
        Incident incident = createIncident(true);
        when(incidentRepository.findById(action.getIncidentId())).thenReturn(Optional.of(incident));
        when(feedbackStore.saveFeedback(any())).thenAnswer(inv -> inv.getArgument(0));

        service.processFeedback(action);
        verify(feedbackStore).saveFeedback(any(FeedbackRecord.class));
    }

    private Incident createIncident(boolean groundTruth) {
        return new Incident(UUID.randomUUID(), 1, SeverityLevel.HIGH, "test", "desc",
                Map.of("cpu", 85.0), "1.1.1.1", "2.2.2.2", groundTruth, List.of(), Instant.now());
    }

    private OperatorAction createAction(Decision decision) {
        return new OperatorAction(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
                decision, decision == Decision.MODIFY ? "fix" : null, 3000, "tester");
    }
}
