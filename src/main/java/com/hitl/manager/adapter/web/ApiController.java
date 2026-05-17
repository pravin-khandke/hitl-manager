package com.hitl.manager.adapter.web;

import com.hitl.manager.adapter.web.dto.*;
import com.hitl.manager.domain.model.*;
import com.hitl.manager.domain.port.*;
import com.hitl.manager.domain.service.*;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api")
public class ApiController {
    private final MetricsCalculationService metricsService;
    private final FeedbackStore feedbackStore;
    private final IncidentRepository incidentRepository;
    private final IncidentEvaluationService evaluationService;

    public ApiController(MetricsCalculationService metricsService,
                          FeedbackStore feedbackStore, IncidentRepository incidentRepository,
                          IncidentEvaluationService evaluationService) {
        this.metricsService = metricsService;
        this.feedbackStore = feedbackStore;
        this.incidentRepository = incidentRepository;
        this.evaluationService = evaluationService;
    }

    @GetMapping("/metrics/blocks")
    public List<BlockMetricsDto> blockMetrics() {
        return metricsService.computeAllBlocks().stream()
                .map(b -> new BlockMetricsDto(b.getBlockNumber(), b.getAiAccuracy(),
                        b.getHumanOverrideRate(), b.getFalsePositiveRate(),
                        b.getResolutionTimeMin(), b.getTotalIncidents(), b.getProcessedCount()))
                .toList();
    }

    @GetMapping("/metrics/severity")
    public Map<String, Object> severityBreakdown() {
        Map<SeverityLevel, Map<String, Object>> breakdown = metricsService.computeSeverityBreakdown();
        Map<String, Object> result = new LinkedHashMap<>();
        breakdown.forEach((k, v) -> result.put(k.name(), v));
        return result;
    }

    @GetMapping("/metrics/equations")
    public Map<String, Double> equations() {
        return metricsService.computeEquations();
    }

    @GetMapping("/charts/scatter")
    public Map<String, Object> scatterData() {
        List<ChartDataDto.ScatterPoint> points = new ArrayList<>();
        for (int block = 1; block <= 5; block++) {
            List<OperatorAction> actions = feedbackStore.findActionsByBlock(block);
            for (OperatorAction action : actions) {
                incidentRepository.findById(action.getIncidentId()).ifPresent(incident -> {
                    AISuggestion suggestion = evaluationService.evaluate(incident);
                    points.add(new ChartDataDto.ScatterPoint(
                            suggestion.getConfidenceScore() * 100,
                            action.getResponseTimeMs() / 1000.0, 5));
                });
            }
        }
        return Map.of("points", points);
    }

    @GetMapping("/charts/mesh")
    public Map<String, Object> meshData() {
        List<ChartDataDto.ScatterPoint> points = new ArrayList<>();
        for (int block = 1; block <= 5; block++) {
            List<OperatorAction> actions = feedbackStore.findActionsByBlock(block);
            int feedbackVolume = actions.size();
            for (OperatorAction action : actions) {
                double accuracy = evaluationService.getModel().getAccuracy();
                double experience = (action.getResponseTimeMs() < 5000) ? 0.8 : 0.4;
                points.add(new ChartDataDto.ScatterPoint(
                        feedbackVolume, experience * 100, accuracy * 50));
            }
        }
        return Map.of("points", points);
    }

    @GetMapping("/incidents")
    public Map<String, Object> incidents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) Integer block,
            @RequestParam(required = false) String severity) {
        List<Incident> filtered = incidentRepository.findAll();
        if (block != null) filtered = incidentRepository.findByBlock(block);
        if (severity != null) filtered = filtered.stream()
                .filter(i -> i.getSeverity().name().equalsIgnoreCase(severity)).toList();

        int start = page * size;
        int end = Math.min(start + size, filtered.size());
        List<Incident> pageItems = start < filtered.size() ? filtered.subList(start, end) : List.of();

        return Map.of(
                "incidents", pageItems,
                "totalPages", (int) Math.ceil((double) filtered.size() / size),
                "currentPage", page,
                "totalCount", filtered.size()
        );
    }
}
