package com.hitl.manager.adapter.model;

import com.hitl.manager.domain.port.AIModel;
import com.hitl.manager.domain.port.AIModelContractTest;

class RuleBasedModelTest extends AIModelContractTest {
    @Override
    protected AIModel createModel() {
        return new RuleBasedModel(new ModelConfig());
    }
}
