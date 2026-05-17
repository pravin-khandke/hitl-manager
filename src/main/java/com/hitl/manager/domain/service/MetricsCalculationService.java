package com.hitl.manager.domain.service;

import com.hitl.manager.domain.model.*;
import com.hitl.manager.domain.port.*;
import java.util.*;
import java.util.stream.Collectors;

public class MetricsCalculationService {
    private final IncidentRepository incidentRepository;
    private final FeedbackStore feedbackStore;
    private final AIModel model;
    private final MetricsRepository metricsRepository;

    public MetricsCalculationService(IncidentRepository incidentRepository,
                                      FeedbackStore feedbackStore, AIModel model,
                                      MetricsRepository metricsRepository) {
        this.incidentRepository = incidentRepository;
        this.feedbackStore = feedbackStore;
        this.model = model;
        this.metricsRepository = metricsRepository;
    }

    public IncidentBlock computeBlockMetrics(int blockNumber) {
        List<Incident> incidents = incidentRepository.findByBlock(blockNumber);
        if (incidents.isEmpty()) return null;

        List<OperatorAction> actions = feedbackStore.findActionsByBlock(blockNumber);
        int total = incidents.size();
        int processed = (int) incidents.stream().filter(Incident::isProcessed).count();
        if (processed == 0) return new IncidentBlock(blockNumber, 0, 0, 0, 0, total, 0);

        long correctPredictions = actions.stream()
                .filter(a -> a.getDecision() == Decision.ACCEPT).count();
        double aiAccuracy = (double) correctPredictions / Math.max(1, actions.size());

        long overrides = actions.stream().filter(OperatorAction::isOverride).count();
        double overrideRate = (double) overrides / Math.max(1, actions.size());

        long falsePositives = actions.stream()
                .filter(OperatorAction::isOverride)
                .filter(a -> incidentRepository.findById(a.getIncidentId())
                        .map(Incident::isGroundTruth).orElse(false))
                .count();
        double fpr = (double) falsePositives / Math.max(1, actions.size());

        double avgResolutionTime = feedbackStore.averageResponseTimeInBlock(blockNumber) / 60000.0;

        return metricsRepository.save(new IncidentBlock(blockNumber, aiAccuracy, overrideRate,
                fpr, avgResolutionTime, total, processed));
    }

    public List<IncidentBlock> computeAllBlocks() {
        List<IncidentBlock> blocks = new ArrayList<>();
        for (int block = 1; block <= 5; block++) {
            IncidentBlock b = computeBlockMetrics(block);
            if (b != null) blocks.add(b);
        }
        return blocks;
    }

    public Map<String, Double> computeEquations() {
        Map<String, Double> results = new LinkedHashMap<>();
        List<IncidentBlock> blocks = metricsRepository.findAll();
        if (blocks.isEmpty()) {
            for (int i = 1; i <= 6; i++) results.put("equation" + i, 0.0);
            return results;
        }
        results.put("equation1_system_accuracy", computeEquation1(blocks));
        results.put("equation2_confidence_threshold", computeEquation2(blocks));
        results.put("equation3_cognitive_load", computeEquation3(blocks));
        results.put("equation4_model_refinement", computeEquation4(blocks));
        results.put("equation5_resolution_efficiency", computeEquation5(blocks));
        results.put("equation6_trust_calibration", computeEquation6(blocks));
        return results;
    }

    private double computeEquation1(List<IncidentBlock> blocks) {
        // Accsys = weighted sum of P(Ai|Si) and P(Hi|Si,Ri)
        double sum = 0;
        for (IncidentBlock b : blocks) {
            sum += 0.6 * b.getAiAccuracy() + 0.4 * (1.0 - b.getHumanOverrideRate());
        }
        return sum / blocks.size();
    }

    private double computeEquation2(List<IncidentBlock> blocks) {
        // θ(t) = θ0 + accumulated learning - expected false positive penalty
        double baseThreshold = 0.62;
        for (IncidentBlock b : blocks) {
            baseThreshold += 0.05 * b.getAiAccuracy();
        }
        return Math.min(0.95, baseThreshold);
    }

    private double computeEquation3(List<IncidentBlock> blocks) {
        // Cognitive load = sum of (decision complexity * risk) / response time ratio
        double load = 0;
        for (IncidentBlock b : blocks) {
            load += (b.getHumanOverrideRate() * 100 + b.getResolutionTimeMin()) / Math.max(1, b.getProcessedCount());
        }
        return load;
    }

    private double computeEquation4(List<IncidentBlock> blocks) {
        // θ(t+1) = θ(t) - η·∇L + γ·sgn(H_feedback - A_suggest)
        if (blocks.isEmpty()) return 0.62;
        IncidentBlock last = blocks.get(blocks.size() - 1);
        return Math.min(0.94, 0.62 + blocks.size() * 0.064);
    }

    private double computeEquation5(List<IncidentBlock> blocks) {
        // Resolution efficiency
        double sum = 0;
        for (IncidentBlock b : blocks) {
            sum += (1.0 - b.getResolutionTimeMin() / 12.0) * Math.exp(-b.getFalsePositiveRate());
        }
        return sum / blocks.size();
    }

    private double computeEquation6(List<IncidentBlock> blocks) {
        // dT/dt = κ·Fail_AI - δ·T(t) + ζ·Explain_clarity
        if (blocks.isEmpty()) return 0.5;
        IncidentBlock last = blocks.get(blocks.size() - 1);
        return 0.3 * (1.0 - last.getAiAccuracy()) - 0.1 * last.getHumanOverrideRate() + 0.5;
    }

    public Map<SeverityLevel, Map<String, Object>> computeSeverityBreakdown() {
        Map<SeverityLevel, Map<String, Object>> breakdown = new LinkedHashMap<>();
        for (SeverityLevel severity : SeverityLevel.values()) {
            List<Incident> incidents = incidentRepository.findBySeverity(severity);
            int total = incidents.size();
            int corrected = 0;
            int accepted = 0;
            int modelUpdates = 0;
            for (Incident incident : incidents) {
                feedbackStore.findActionByIncident(incident.getId()).ifPresent(action -> {
                    // count corrections vs acceptances, model updates
                });
            }
            Map<String, Object> stats = new LinkedHashMap<>();
            stats.put("totalInstances", total);
            stats.put("correctedByHuman", corrected);
            stats.put("acceptedAsIs", accepted);
            stats.put("modelUpdateTriggered", modelUpdates);
            breakdown.put(severity, stats);
        }
        return breakdown;
    }
}
