package com.hitl.manager.config;

import com.hitl.manager.domain.model.*;
import com.hitl.manager.domain.port.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.Instant;
import java.util.*;

@Component
public class DataLoader implements CommandLineRunner {
    private final IncidentRepository incidentRepository;
    private final MetricsRepository metricsRepository;

    public DataLoader(IncidentRepository incidentRepository, MetricsRepository metricsRepository) {
        this.incidentRepository = incidentRepository;
        this.metricsRepository = metricsRepository;
    }

    @Override
    public void run(String... args) {
        if (incidentRepository.count() > 0) return;

        List<Incident> incidents = new ArrayList<>();
        Random random = new Random(42);

        String[] types = {"unauthorized_access", "maintenance_spike", "ddos_attempt",
                "config_change", "disk_full", "cpu_spike", "memory_leak", "network_scan"};
        SeverityLevel[] severities = SeverityLevel.values();
        String[] resolutions = {"restart_service", "block_ip", "increase_capacity",
                "apply_patch", "adjust_firewall", "clear_cache"};

        for (int i = 1; i <= 215; i++) {
            int blockNumber = ((i - 1) / 43) + 1;
            boolean isFalsePositive = random.nextDouble() < 0.30;

            Map<String, Double> metrics = new HashMap<>();
            metrics.put("cpu", 20.0 + random.nextDouble() * 80.0);
            metrics.put("memory", 30.0 + random.nextDouble() * 60.0);
            metrics.put("network", 10.0 + random.nextDouble() * 90.0);

            incidents.add(new Incident(
                    UUID.randomUUID(), blockNumber,
                    severities[random.nextInt(severities.length)],
                    types[random.nextInt(types.length)],
                    "Incident #" + i + ": " + types[random.nextInt(types.length)] + " detected at node "
                            + random.nextInt(100),
                    metrics,
                    "192.168." + random.nextInt(256) + "." + random.nextInt(256),
                    "10.0." + random.nextInt(256) + "." + random.nextInt(256),
                    !isFalsePositive,
                    List.of(resolutions[random.nextInt(resolutions.length)]),
                    Instant.now().minusSeconds(3600L * (215 - i))
            ));
        }

        incidentRepository.saveAll(incidents);
        System.out.println("Loaded 215 incidents across 5 blocks (seed=42)");
    }
}
