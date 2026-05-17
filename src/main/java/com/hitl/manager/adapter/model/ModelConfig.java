package com.hitl.manager.adapter.model;

public class ModelConfig {
    private double cpuThresholdHigh = 70.0;
    private double memoryThresholdHigh = 75.0;
    private double cpuCriticalThreshold = 90.0;
    private double memoryCriticalThreshold = 90.0;
    private double networkSpikeThreshold = 80.0;
    private String version = "1.0.0";

    public double getCpuThresholdHigh() { return cpuThresholdHigh; }
    public void setCpuThresholdHigh(double v) { this.cpuThresholdHigh = v; }
    public double getMemoryThresholdHigh() { return memoryThresholdHigh; }
    public void setMemoryThresholdHigh(double v) { this.memoryThresholdHigh = v; }
    public double getCpuCriticalThreshold() { return cpuCriticalThreshold; }
    public void setCpuCriticalThreshold(double v) { this.cpuCriticalThreshold = v; }
    public double getMemoryCriticalThreshold() { return memoryCriticalThreshold; }
    public void setMemoryCriticalThreshold(double v) { this.memoryCriticalThreshold = v; }
    public double getNetworkSpikeThreshold() { return networkSpikeThreshold; }
    public void setNetworkSpikeThreshold(double v) { this.networkSpikeThreshold = v; }
    public String getVersion() { return version; }
    public void setVersion(String v) { this.version = v; }

    public ModelConfig copy() {
        ModelConfig c = new ModelConfig();
        c.cpuThresholdHigh = this.cpuThresholdHigh;
        c.memoryThresholdHigh = this.memoryThresholdHigh;
        c.cpuCriticalThreshold = this.cpuCriticalThreshold;
        c.memoryCriticalThreshold = this.memoryCriticalThreshold;
        c.networkSpikeThreshold = this.networkSpikeThreshold;
        c.version = this.version;
        return c;
    }
}
