package com.hitl.manager.config;

import com.hitl.manager.domain.port.*;
import com.hitl.manager.domain.service.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfiguration {
    @Bean
    public IncidentEvaluationService incidentEvaluationService(AIModel model) {
        return new IncidentEvaluationService(model);
    }

    @Bean
    public OperatorInteractionService operatorInteractionService(FeedbackStore feedbackStore) {
        return new OperatorInteractionService(feedbackStore);
    }

    @Bean
    public FeedbackProcessingService feedbackProcessingService(
            AIModel model, FeedbackStore feedbackStore, IncidentRepository incidentRepository) {
        return new FeedbackProcessingService(model, feedbackStore, incidentRepository);
    }

    @Bean
    public MetricsCalculationService metricsCalculationService(
            IncidentRepository incidentRepository, FeedbackStore feedbackStore,
            AIModel model, MetricsRepository metricsRepository) {
        return new MetricsCalculationService(incidentRepository, feedbackStore, model, metricsRepository);
    }

    @Bean
    public BlockProgressionService blockProgressionService(
            IncidentRepository incidentRepository, MetricsRepository metricsRepository) {
        return new BlockProgressionService(incidentRepository, metricsRepository);
    }
}
