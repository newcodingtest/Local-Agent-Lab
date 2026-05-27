# Agent Lab

Spring AI + Ollama 기반 개인 AI Agent Playground.

로컬 LLM(Ollama)과 Spring AI를 이용해
여러 도메인의 AI 에이전트를 실험하고 구축하는 프로젝트입니다.

현재는 Daily English Content Agent를 구현 중이며,
향후 Swagger/OpenAPI 분석, 코드 리뷰, Resume Assistant 등
다양한 AI Agent를 추가할 예정입니다.

---

# Features

- Spring AI 기반 LLM 통합
- Ollama Local Model 연동
- Multi Model Routing
- Agent Pipeline Architecture
- Domain-based Agent Structure
- JSON Validation Pipeline
- Prompt Builder Pattern

---

# Architecture

```text
Client
  ↓
Spring AI Agent Server
  ↓
Agent Pipeline
  ├── Content Generation
  ├── English Review
  ├── JSON Validation
  └── Final Rewrite
  ↓
Ollama
  ├── qwen3:30b
  ├── gemma3:27b
  └── qwen3.5:9b
```

---

# Tech Stack

- Java 21
- Spring Boot 3
- Spring AI
- Ollama
- Gradle
- Lombok

---

# Current Agents

## Daily English Content Agent

영어 학습 콘텐츠를 생성하는 AI Agent.

### Pipeline

```text
Draft Generation
  → English Review
  → JSON Validation
  → Final Rewrite
```

### Example Request

```bash
curl -X POST http://localhost:8080/api/english/daily/generate \
  -H "Content-Type: application/json" \
  -d '{
    "contentType": "GRAMMAR",
    "year": 2026,
    "month": 5,
    "day": 27,
    "topic": "Present Perfect"
  }'
```

---

# Project Structure

```text
com.agentlab.ai
 ├── common
 │   └── llm
 │
 ├── english
 │   └── daily
 │
 ├── swagger
 │
 ├── resume
 │
 └── code
```

---

# Run Ollama

```bash
ollama serve
```

---

# Install Models

```bash
ollama pull qwen3:30b
ollama pull gemma3:27b
ollama pull qwen3.5:9b
```

---

# Run Application

```bash
./gradlew bootRun
```

---

# Future Plans

- Swagger/OpenAPI Agent
- Resume Assistant Agent
- Code Review Agent
- MCP Integration
- Scheduler-based Auto Generation
- Vercel + Mac Mini Integration
- Multi-Agent Collaboration
- RAG Support

---