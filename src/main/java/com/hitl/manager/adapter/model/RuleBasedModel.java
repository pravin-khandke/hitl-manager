package com.hitl.manager.adapter.model;

import com.hitl.manager.domain.model.*;
import com.hitl.manager.domain.port.AIModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class RuleBasedModel implements AIModel {
    private static final Logger log = LoggerFactory.getLogger(RuleBasedModel.class);
    private ModelConfig config;
    private final AtomicInteger predictionCount = new AtomicInteger(0);
    private final AtomicInteger learningCount = new AtomicInteger(0);
    private double currentAccuracy = 0.62;

    public RuleBasedModel(ModelConfig config) {
        this.config = config.copy();
    }

    @Override
    public AISuggestion predict(Incident incident) {
        predictionCount.incrementAndGet();
        Map<String, Double> metrics = incident.getResourceMetrics();
        double cpu = metrics.getOrDefault("cpu", 0.0);
        double memory = metrics.getOrDefault("memory", 0.0);
        double network = metrics.getOrDefault("network", 0.0);

        String action;
        double confidence;
        String explanation;

        if (cpu > config.getCpuCriticalThreshold() || memory > config.getMemoryCriticalThreshold()) {
            action = "Escalate to security team — critical resource threshold breached";
            confidence = 0.85 + (Math.random() * 0.10);
            explanation = "CPU at " + String.format("%.1f", cpu) + "% (threshold: "
                    + config.getCpuCriticalThreshold() + "%), Memory at "
                    + String.format("%.1f", memory) + "% (threshold: "
                    + config.getMemoryCriticalThreshold() + "%)";
        } else if (cpu > config.getCpuThresholdHigh()) {
            action = "Investigate CPU spike — possible unauthorized process";
            confidence = 0.60 + (Math.random() * 0.15);
            explanation = "CPU at " + String.format("%.1f", cpu) + "% exceeds threshold of "
                    + config.getCpuThresholdHigh() + "%";
        } else if (network > config.getNetworkSpikeThreshold()) {
            action = "Monitor network traffic — unusual outbound connection pattern";
            confidence = 0.55 + (Math.random() * 0.15);
            explanation = "Network at " + String.format("%.1f", network)
                    + "% exceeds threshold of " + config.getNetworkSpikeThreshold() + "%";
        } else {
            action = "Log incident — low severity, routine monitoring sufficient";
            confidence = 0.65 + (Math.random() * 0.15);
            explanation = "All metrics within normal operating range";
        }

        return new AISuggestion(UUID.randomUUID(), incident.getId(), action,
                Math.min(0.99, confidence), explanation, config.getVersion());
    }

    @Override
    public void learn(Incident incident, OperatorAction action) {
        if (!action.isOverride()) return;
        learningCount.incrementAndGet();

        if (action.getDecision() == Decision.REJECT && incident.isGroundTruth()) {
            config.setCpuThresholdHigh(config.getCpuThresholdHigh() + 3.0);
            config.setMemoryThresholdHigh(config.getMemoryThresholdHigh() + 2.0);
        } else if (action.getDecision() == Decision.MODIFY && !incident.isGroundTruth()) {
            config.setCpuThresholdHigh(config.getCpuThresholdHigh() - 2.0);
        }

        currentAccuracy = Math.min(0.94, currentAccuracy + 0.008);
        config.setVersion("1." + learningCount.get() + ".0");
        log.debug("Model learned. Accuracy: {}, CPU threshold: {}", currentAccuracy, config.getCpuThresholdHigh());
    }

    @Override
    public double getAccuracy() { return currentAccuracy; }

    @Override
    public double getConfidence(Incident incident) {
        Map<String, Double> metrics = incident.getResourceMetrics();
        double cpu = metrics.getOrDefault("cpu", 0.0);
        if (cpu > config.getCpuCriticalThreshold()) return 0.90;
        if (cpu > config.getCpuThresholdHigh()) return 0.65;
        return 0.70;
    }

    @Override
    public ModelMetadata getMetadata() {
        return new ModelMetadata("rule-based", config.getVersion(),
                predictionCount.get(), learningCount.get(), currentAccuracy);
    }

    @Override
    public void reset() {
        config = new ModelConfig();
        predictionCount.set(0);
        learningCount.set(0);
        currentAccuracy = 0.62;
    }
}
