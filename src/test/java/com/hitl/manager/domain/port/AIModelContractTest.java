package com.hitl.manager.domain.port;

import com.hitl.manager.domain.model.*;
import org.junit.jupiter.api.*;
import java.time.Instant;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public abstract class AIModelContractTest {
    protected abstract AIModel createModel();

    private AIModel model;

    @BeforeEach
    void setUp() { model = createModel(); }

    @Test
    void predictReturnsSuggestionForAnyIncident() {
        Incident incident = createTestIncident();
        AISuggestion suggestion = model.predict(incident);
        assertNotNull(suggestion);
        assertNotNull(suggestion.getSuggestedAction());
        assertTrue(suggestion.getConfidenceScore() >= 0 && suggestion.getConfidenceScore() <= 1);
    }

    @Test
    void accuracyStartsBelowOne() {
        assertTrue(model.getAccuracy() < 1.0);
    }

    @Test
    void learnIncreasesAccuracyForOverride() {
        Incident incident = createTestIncident();
        double before = model.getAccuracy();
        OperatorAction action = new OperatorAction(UUID.randomUUID(), UUID.randomUUID(),
                incident.getId(), Decision.MODIFY, "corrected action", 5000, "tester");
        model.learn(incident, action);
        assertTrue(model.getAccuracy() > before, "Learning should increase accuracy");
    }

    @Test
    void learnDoesNotChangeAccuracyForAccept() {
        Incident incident = createTestIncident();
        double before = model.getAccuracy();
        OperatorAction action = new OperatorAction(UUID.randomUUID(), UUID.randomUUID(),
                incident.getId(), Decision.ACCEPT, null, 2000, "tester");
        model.learn(incident, action);
        assertEquals(before, model.getAccuracy(), 0.001);
    }

    @Test
    void resetRestoresInitialState() {
        Incident incident = createTestIncident();
        OperatorAction action = new OperatorAction(UUID.randomUUID(), UUID.randomUUID(),
                incident.getId(), Decision.REJECT, null, 3000, "tester");
        model.learn(incident, action);
        model.reset();
        assertEquals(0.62, model.getAccuracy(), 0.01);
    }

    @Test
    void confidenceIsBetweenZeroAndOne() {
        double confidence = model.getConfidence(createTestIncident());
        assertTrue(confidence >= 0 && confidence <= 1);
    }

    @Test
    void metadataReturnsCorrectImplementation() {
        ModelMetadata metadata = model.getMetadata();
        assertNotNull(metadata);
        assertEquals("rule-based", metadata.getImplementation());
        assertNotNull(metadata.getVersion());
    }

    @Test
    void multiplePredictionsIncreaseCount() {
        model.predict(createTestIncident());
        model.predict(createTestIncident());
        assertTrue(model.getMetadata().getTotalPredictions() >= 2);
    }

    private Incident createTestIncident() {
        return new Incident(UUID.randomUUID(), 1, SeverityLevel.HIGH, "cpu_spike",
                "Test incident description",
                Map.of("cpu", 85.0, "memory", 45.0, "network", 20.0),
                "192.168.1.1", "10.0.0.1", true, List.of("restart"), Instant.now());
    }
}
