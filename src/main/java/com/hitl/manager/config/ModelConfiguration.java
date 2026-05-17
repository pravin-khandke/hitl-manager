package com.hitl.manager.config;

import com.hitl.manager.adapter.model.ModelConfig;
import com.hitl.manager.adapter.model.RuleBasedModel;
import com.hitl.manager.domain.port.AIModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelConfiguration {
    @Value("${hitl.model.implementation}")
    private String implementation;

    @Bean
    public AIModel aiModel() {
        if ("rule-based".equals(implementation)) {
            return new RuleBasedModel(new ModelConfig());
        }
        throw new IllegalStateException("Unknown model implementation: " + implementation);
    }
}
