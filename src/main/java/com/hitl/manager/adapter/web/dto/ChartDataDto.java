package com.hitl.manager.adapter.web.dto;

import java.util.List;

public record ChartDataDto(List<ScatterPoint> points) {
    public record ScatterPoint(double x, double y, double r) {}
}
