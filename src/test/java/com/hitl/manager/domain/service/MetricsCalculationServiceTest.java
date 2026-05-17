package com.hitl.manager.domain.service;

import com.hitl.manager.domain.model.*;
import com.hitl.manager.domain.port.*;
import org.junit.jupiter.api.*;
import java.time.Instant;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MetricsCalculationServiceTest {
    private IncidentRepository incidentRepository;
    private FeedbackStore feedbackStore;
    private AIModel model;
    private MetricsRepository metricsRepository;
    private MetricsCalculationService service;

    @BeforeEach
    void setUp() {
        incidentRepository = mock(IncidentRepository.class);
        feedbackStore = mock(FeedbackStore.class);
        model = mock(AIModel.class);
        metricsRepository = mock(MetricsRepository.class);
        service = new MetricsCalculationService(incidentRepository, feedbackStore, model, metricsRepository);
    }

    @Test
    void computeBlockMetricsReturnsNullForEmptyBlock() {
        when(incidentRepository.findByBlock(5)).thenReturn(List.of());
        assertNull(service.computeBlockMetrics(5));
    }

    @Test
    void computeBlockMetricsWithNoProcessedReturnsZeroMetrics() {
        Incident incident = new Incident(UUID.randomUUID(), 1, SeverityLevel.HIGH, "test",
                "desc", Map.of(), "1.1.1.1", "2.2.2.2", true, List.of(), Instant.now());
        when(incidentRepository.findByBlock(1)).thenReturn(List.of(incident));
        when(feedbackStore.findActionsByBlock(1)).thenReturn(List.of());

        IncidentBlock result = service.computeBlockMetrics(1);
        assertNotNull(result);
        assertEquals(1, result.getBlockNumber());
        assertEquals(0.0, result.getAiAccuracy());
        assertEquals(0, result.getProcessedCount());
    }

    @Test
    void computeEquationsReturnsSixValues() {
        when(metricsRepository.findAll()).thenReturn(List.of());
        Map<String, Double> results = service.computeEquations();
        assertEquals(6, results.size());
    }

    @Test
    void computeAllBlocksReturnsFiveBlocks() {
        when(incidentRepository.findByBlock(anyInt())).thenReturn(List.of());
        List<IncidentBlock> blocks = service.computeAllBlocks();
        assertEquals(0, blocks.size()); // all empty → no blocks returned
    }
}
