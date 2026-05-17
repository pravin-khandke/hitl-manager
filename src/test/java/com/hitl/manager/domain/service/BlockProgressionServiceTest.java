package com.hitl.manager.domain.service;

import com.hitl.manager.domain.model.*;
import com.hitl.manager.domain.port.*;
import org.junit.jupiter.api.*;
import java.time.Instant;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BlockProgressionServiceTest {
    private IncidentRepository incidentRepository;
    private MetricsRepository metricsRepository;
    private BlockProgressionService service;

    @BeforeEach
    void setUp() {
        incidentRepository = mock(IncidentRepository.class);
        metricsRepository = mock(MetricsRepository.class);
        service = new BlockProgressionService(incidentRepository, metricsRepository);
    }

    @Test
    void currentBlockIsOneWhenNoneProcessed() {
        when(incidentRepository.countByBlock(1)).thenReturn(43L);
        when(incidentRepository.findByBlock(1)).thenReturn(List.of(createIncident(false)));
        assertEquals(1, service.getCurrentBlock());
    }

    @Test
    void isAllCompleteWhenNoUnprocessed() {
        when(incidentRepository.findUnprocessed()).thenReturn(List.of());
        assertTrue(service.isAllComplete());
    }

    @Test
    void isNotAllCompleteWhenUnprocessedExist() {
        when(incidentRepository.findUnprocessed())
                .thenReturn(List.of(createIncident(false)));
        assertFalse(service.isAllComplete());
    }

    @Test
    void nextUnprocessedReturnsEarliest() {
        Incident older = new Incident(UUID.randomUUID(), 1, SeverityLevel.LOW, "t", "d",
                Map.of(), "1.1.1.1", "2.2.2.2", true, List.of(),
                Instant.now().minusSeconds(7200));
        Incident newer = new Incident(UUID.randomUUID(), 1, SeverityLevel.LOW, "t", "d",
                Map.of(), "1.1.1.1", "2.2.2.2", true, List.of(),
                Instant.now().minusSeconds(3600));
        when(incidentRepository.findUnprocessed()).thenReturn(List.of(newer, older));

        Optional<Incident> next = service.nextUnprocessed();
        assertTrue(next.isPresent());
        assertEquals(older.getId(), next.get().getId()); // older = earlier createdAt
    }

    private Incident createIncident(boolean processed) {
        Incident i = new Incident(UUID.randomUUID(), 1, SeverityLevel.MEDIUM, "type", "desc",
                Map.of(), "1.1.1.1", "2.2.2.2", true, List.of(), Instant.now());
        if (processed) i.markProcessed();
        return i;
    }
}
