package com.hitl.manager.adapter.persistence;

import com.hitl.manager.domain.model.*;
import com.hitl.manager.domain.port.IncidentRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import java.time.Instant;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Import({PostgresIncidentRepository.class, PostgresFeedbackStore.class, PostgresMetricsRepository.class})
class PostgresIncidentRepositoryTest {
    @Autowired
    private IncidentRepository repository;

    @Test
    void savesAndFindsIncidentsByBlock() {
        Incident incident = new Incident(UUID.randomUUID(), 1, SeverityLevel.HIGH,
                "test", "test desc", Map.of("cpu", 50.0),
                "192.168.1.1", "10.0.0.1", true, List.of("fix"), Instant.now());
        repository.save(incident);

        List<Incident> found = repository.findByBlock(1);
        assertEquals(1, found.size());
        assertEquals("test", found.get(0).getType());
    }

    @Test
    void findsUnprocessedIncidents() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        Incident i1 = createIncident(id1, false);
        Incident i2 = createIncident(id2, true);
        repository.save(i1);
        repository.save(i2);

        List<Incident> unprocessed = repository.findUnprocessed();
        assertEquals(1, unprocessed.size());
        assertEquals(id1, unprocessed.get(0).getId());
    }

    @Test
    void countReturnsCorrectNumber() {
        assertEquals(0, repository.count());
        repository.save(createIncident(UUID.randomUUID(), false));
        assertEquals(1, repository.count());
    }

    @Test
    void saveAllBulkInserts() {
        List<Incident> incidents = List.of(
                createIncident(UUID.randomUUID(), false),
                createIncident(UUID.randomUUID(), false)
        );
        repository.saveAll(incidents);
        assertEquals(2, repository.count());
    }

    @Test
    void findByBlockReturnsOnlyMatchingBlock() {
        repository.save(new Incident(UUID.randomUUID(), 2, SeverityLevel.LOW, "t", "d",
                Map.of(), "1.1.1.1", "2.2.2.2", true, List.of(), Instant.now()));
        repository.save(new Incident(UUID.randomUUID(), 3, SeverityLevel.LOW, "t", "d",
                Map.of(), "1.1.1.1", "2.2.2.2", true, List.of(), Instant.now()));

        assertEquals(1, repository.findByBlock(2).size());
        assertEquals(1, repository.findByBlock(3).size());
        assertEquals(0, repository.findByBlock(1).size());
    }

    @Test
    void findBySeverityFiltersCorrectly() {
        repository.save(new Incident(UUID.randomUUID(), 1, SeverityLevel.CRITICAL, "t", "d",
                Map.of(), "1.1.1.1", "2.2.2.2", true, List.of(), Instant.now()));
        repository.save(new Incident(UUID.randomUUID(), 1, SeverityLevel.LOW, "t", "d",
                Map.of(), "1.1.1.1", "2.2.2.2", true, List.of(), Instant.now()));

        assertEquals(1, repository.findBySeverity(SeverityLevel.CRITICAL).size());
        assertEquals(1, repository.findBySeverity(SeverityLevel.LOW).size());
    }

    private Incident createIncident(UUID id, boolean processed) {
        Incident i = new Incident(id, 1, SeverityLevel.MEDIUM, "type", "desc",
                Map.of(), "1.1.1.1", "2.2.2.2", true, List.of(), Instant.now());
        if (processed) i.markProcessed();
        return i;
    }
}
