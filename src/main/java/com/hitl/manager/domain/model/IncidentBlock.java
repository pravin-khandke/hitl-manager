package com.hitl.manager.domain.model;

public class IncidentBlock {
    private final int blockNumber;
    private final double aiAccuracy;
    private final double humanOverrideRate;
    private final double falsePositiveRate;
    private final double resolutionTimeMin;
    private int totalIncidents;
    private int processedCount;

    public IncidentBlock(int blockNumber, double aiAccuracy, double humanOverrideRate,
                         double falsePositiveRate, double resolutionTimeMin,
                         int totalIncidents, int processedCount) {
        this.blockNumber = blockNumber;
        this.aiAccuracy = aiAccuracy;
        this.humanOverrideRate = humanOverrideRate;
        this.falsePositiveRate = falsePositiveRate;
        this.resolutionTimeMin = resolutionTimeMin;
        this.totalIncidents = totalIncidents;
        this.processedCount = processedCount;
    }

    public int getBlockNumber() { return blockNumber; }
    public double getAiAccuracy() { return aiAccuracy; }
    public double getHumanOverrideRate() { return humanOverrideRate; }
    public double getFalsePositiveRate() { return falsePositiveRate; }
    public double getResolutionTimeMin() { return resolutionTimeMin; }
    public int getTotalIncidents() { return totalIncidents; }
    public int getProcessedCount() { return processedCount; }
}
