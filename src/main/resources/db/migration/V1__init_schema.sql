CREATE TYPE severity_enum AS ENUM ('CRITICAL', 'HIGH', 'MEDIUM', 'LOW', 'INFORMATIONAL');
CREATE TYPE decision_enum AS ENUM ('ACCEPT', 'MODIFY', 'REJECT');

CREATE TABLE incidents (
    id UUID PRIMARY KEY,
    block_number INT NOT NULL,
    severity severity_enum NOT NULL,
    type VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    resource_metrics JSONB NOT NULL DEFAULT '{}',
    source_ip VARCHAR(45),
    destination_ip VARCHAR(45),
    ground_truth BOOLEAN NOT NULL,
    resolution_tags TEXT[],
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    processed BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE INDEX idx_incidents_block ON incidents(block_number);
CREATE INDEX idx_incidents_severity ON incidents(severity);
CREATE INDEX idx_incidents_processed ON incidents(processed);

CREATE TABLE ai_suggestions (
    id UUID PRIMARY KEY,
    incident_id UUID NOT NULL REFERENCES incidents(id),
    suggested_action VARCHAR(500) NOT NULL,
    confidence_score DOUBLE PRECISION NOT NULL,
    explanation TEXT,
    model_version VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_suggestions_incident ON ai_suggestions(incident_id);

CREATE TABLE operator_actions (
    id UUID PRIMARY KEY,
    suggestion_id UUID NOT NULL REFERENCES ai_suggestions(id),
    incident_id UUID NOT NULL REFERENCES incidents(id),
    decision decision_enum NOT NULL,
    modification TEXT,
    response_time_ms INT NOT NULL,
    operator_id VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_actions_incident ON operator_actions(incident_id);

CREATE TABLE feedback_records (
    id UUID PRIMARY KEY,
    action_id UUID NOT NULL REFERENCES operator_actions(id),
    model_updated BOOLEAN NOT NULL DEFAULT FALSE,
    parameter_delta JSONB NOT NULL DEFAULT '{}',
    learned_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_feedback_action ON feedback_records(action_id);

CREATE TABLE block_metrics (
    id UUID PRIMARY KEY,
    block_number INT NOT NULL UNIQUE,
    ai_accuracy DOUBLE PRECISION NOT NULL,
    human_override_rate DOUBLE PRECISION NOT NULL,
    false_positive_rate DOUBLE PRECISION NOT NULL,
    resolution_time_min DOUBLE PRECISION NOT NULL,
    computed_at TIMESTAMP NOT NULL DEFAULT NOW()
);
