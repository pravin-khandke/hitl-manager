package com.hitl.manager.adapter.web;

import com.hitl.manager.domain.service.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Disabled("Requires ByteBuddy compatibility with Java 25 — enable after upgrading Spring Boot / Mockito")
@WebMvcTest(DashboardController.class)
class DashboardControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MetricsCalculationService metricsService;

    @MockitoBean
    private BlockProgressionService blockProgression;

    @Test
    void dashboardReturns200() throws Exception {
        when(metricsService.computeAllBlocks()).thenReturn(List.of());
        when(blockProgression.getCurrentBlock()).thenReturn(1);
        when(blockProgression.isAllComplete()).thenReturn(false);

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard"));
    }

    @Test
    void architectureReturns200() throws Exception {
        mockMvc.perform(get("/architecture"))
                .andExpect(status().isOk())
                .andExpect(view().name("architecture"));
    }

    @Test
    void chartsReturns200() throws Exception {
        mockMvc.perform(get("/charts"))
                .andExpect(status().isOk())
                .andExpect(view().name("charts"));
    }

    @Test
    void equationsReturns200() throws Exception {
        when(metricsService.computeEquations()).thenReturn(Map.of());

        mockMvc.perform(get("/equations"))
                .andExpect(status().isOk())
                .andExpect(view().name("equations"));
    }

    @Test
    void dataBrowserReturns200() throws Exception {
        mockMvc.perform(get("/data"))
                .andExpect(status().isOk())
                .andExpect(view().name("data-browser"));
    }
}
