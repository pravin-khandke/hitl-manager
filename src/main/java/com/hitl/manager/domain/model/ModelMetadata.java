package com.hitl.manager.domain.model;

public class ModelMetadata {
    private final String implementation;
    private final String version;
    private final int totalPredictions;
    private final int totalLearnings;
    private final double currentAccuracy;

    public ModelMetadata(String implementation, String version, int totalPredictions,
                         int totalLearnings, double currentAccuracy) {
        this.implementation = implementation;
        this.version = version;
        this.totalPredictions = totalPredictions;
        this.totalLearnings = totalLearnings;
        this.currentAccuracy = currentAccuracy;
    }

    public String getImplementation() { return implementation; }
    public String getVersion() { return version; }
    public int getTotalPredictions() { return totalPredictions; }
    public int getTotalLearnings() { return totalLearnings; }
    public double getCurrentAccuracy() { return currentAccuracy; }
}
