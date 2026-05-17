# HITL-Manager 2.0

> **Note:** This project is provided as a reference implementation to accompany the research paper and does not contain the full production system details. It has been created for educational purposes. If you need more details about this project, please contact me at my GitHub profile.

---

## Introduction

Modern incident management systems increasingly rely on AI-driven automation to triage, classify, and resolve operational events. However, fully autonomous systems introduce risks — false positives, opaque decision-making, and the erosion of operator expertise. The research paper **"Designing Adaptive Human-in-the-Loop Interfaces for Enhanced Collaborative Incident Management"** (Khandke, 2026) investigates a middle ground: systems where AI suggestions and human judgment work in tandem, with the model adapting to operator feedback over time.

### Motivation

The core hypothesis is that an adaptive Human-in-the-Loop (HITL) architecture — where operator decisions (accept, modify, reject) continuously refine the AI model — can achieve better outcomes than either fully manual or fully automated approaches. Key questions the research addresses:

- Can block-by-block operator feedback measurably improve AI accuracy and reduce false positive rates?
- What interface design patterns best support rapid operator review and decision-making?
- How do different feedback mechanisms (rule-based threshold adjustment vs. learned model updates) affect system performance across incident severity levels?

This project implements the experimental apparatus described in the paper: a Spring Boot application with a visualization dashboard, a working HITL simulator, and REST APIs — all rendered server-side with Thymeleaf and HTMX. It reproduces the paper's results using 215 incidents across 5 progression blocks.

---

## Overview

A Spring Boot web application demonstrating the HITL feedback loop from the research. It combines a visualization dashboard, an interactive incident simulator, and REST APIs for chart and metrics data.

**Architecture:** Hexagonal (Ports & Adapters)

**Tech Stack:** Spring Boot 3.4.x, Java 21, Thymeleaf + HTMX 2.x, PostgreSQL 16, Flyway, Chart.js, MathJax

### HITL Feedback Loop

For each incident in the simulator:

```
[Incident arrives]
    → IncidentEvaluationService sends incident to AIModel port
    → AIModel.predict() returns AISuggestion (action + confidence + explanation)
    → UI displays suggestion to operator
    → Operator decides: ACCEPT / MODIFY / REJECT
    → OperatorInteractionService captures OperatorAction (decision + responseTimeMs)
    → FeedbackProcessingService:
        1. Compares AI suggestion vs operator decision
        2. If rejected/modified → triggers AIModel.learn(incident, operatorAction)
        3. Records whether model update was triggered
        4. Persists FeedbackRecord
    → MetricsCalculationService recomputes block KPIs
    → UI polls and refreshes dashboard components via HTMX
```

### Key Features

- **Dashboard** — Live KPIs (Table 1 & 2 from the paper) with HTMX polling
- **Simulator** — Incident queue with AI suggestion cards and Accept/Modify/Reject controls
- **Charts** — Figure 2 (scatter) and Figure 3 (accuracy mesh) via Chart.js
- **Equations** — Equations 1–6 rendered with MathJax and live computed values
- **Pluggable Model** — `AIModel` interface with default `RuleBasedModel`; swap in ML models via config

### Pluggable Model Interface

```java
public interface AIModel {
    AISuggestion predict(Incident incident);
    void learn(Incident incident, OperatorAction action);
    double getAccuracy();
    double getConfidence(Incident incident);
    ModelMetadata getMetadata();
    void reset();
}
```

The default `RuleBasedModel` starts at 62% accuracy / 28% FPR and adjusts thresholds based on operator corrections, reproducing the paper's block-by-block progression. Swap in an `MlModel` implementation (future) via `hitl.model.implementation=...` in config — the rest of the system only sees the `AIModel` interface.

---

## Getting Started

### Prerequisites

- Java 21
- Maven 3.9+
- PostgreSQL 16 (or use Testcontainers for dev)

### Build & Run

```bash
# Start PostgreSQL and create the database
createdb hitl_manager

# Run the application
./mvnw spring-boot:run
```

The app starts at `http://localhost:8080`. Flyway runs migrations on startup. The `DataLoader` pre-loads 215 incidents automatically.

### Configuration

```properties
# application.properties
spring.datasource.url=jdbc:postgresql://localhost:5432/hitl_manager
spring.datasource.username=postgres
spring.datasource.password=your_password
hitl.model.implementation=rule-based
```

---

## Project Structure

```
com.hitl.manager/
├── domain/
│   ├── model/          # Incident, AISuggestion, OperatorAction, FeedbackRecord
│   ├── port/           # IncidentRepository, AIModel, FeedbackStore, MetricsRepository
│   └── service/        # Evaluation, Interaction, Feedback, Metrics, Block Progression
├── adapter/
│   ├── persistence/    # Postgres repositories (JPA)
│   ├── model/          # RuleBasedModel, ModelConfig
│   └── web/            # Controllers + DTOs
├── config/             # ModelConfiguration, DataLoader
└── HITLManagerApplication.java
```

---

## License

This project is provided for educational and reference purposes.
