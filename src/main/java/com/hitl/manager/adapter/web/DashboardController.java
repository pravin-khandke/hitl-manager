package com.hitl.manager.adapter.web;

import com.hitl.manager.domain.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {
    private final MetricsCalculationService metricsService;
    private final BlockProgressionService blockProgression;

    public DashboardController(MetricsCalculationService metricsService,
                                BlockProgressionService blockProgression) {
        this.metricsService = metricsService;
        this.blockProgression = blockProgression;
    }

    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("blocks", metricsService.computeAllBlocks());
        model.addAttribute("currentBlock", blockProgression.getCurrentBlock());
        model.addAttribute("allComplete", blockProgression.isAllComplete());
        return "dashboard";
    }

    @GetMapping("/architecture")
    public String architecture() { return "architecture"; }

    @GetMapping("/charts")
    public String charts() { return "charts"; }

    @GetMapping("/equations")
    public String equations(Model model) {
        model.addAttribute("equationValues", metricsService.computeEquations());
        return "equations";
    }

    @GetMapping("/data")
    public String dataBrowser() { return "data-browser"; }
}
