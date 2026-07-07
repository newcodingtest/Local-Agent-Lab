# Agent Lab

Spring AI + Ollama 기반 개인 AI Agent Playground.

로컬 LLM(Ollama)과 Spring AI를 이용해
여러 도메인의 AI 에이전트를 실험하고 구축하는 프로젝트입니다.

현재는 Daily English Content Agent, Swagger/OpenAPI 분석 Agent, 코드 리뷰 Agent 를 구축해뒀습니다.

[코드리뷰] <br>
https://github.com/newcodingtest/ai-review-test-spring-api/pull/3
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



# 클라이언트 측 필요 MD

AI 코드 리뷰어에게 보다 정확한 피드벡을 받기 위해선 아래와 같이 프로젝트 설명 파일이 필요합니다.

```
client-project/
 ├─ src/
 ├─ build.gradle
 ├─ README.md
 └─ .ai-review/
     ├─ project-profile.md        (프로젝트 개요)
     ├─ architecture-rules.md     (아키텍처 계층 규칙)
     ├─ package-role-map.md       (패키지별 역할)
     ├─ class-role-rules.md       (클래스별 책임)
     ├─ dependency-rules.md       (의존성 허용/금지 규칙)
     ├─ domain-glossary.md        (도메인 용어 사전)
     ├─ coding-conventions.md     (코딩 컨벤션)
     ├─ testing-policy.md         (테스트 작성 기준)
     ├─ error-handling-policy.md  (예외 처리 기준)
     ├─ transaction-policy.md     (트랜잭션 기준)
     ├─ security-policy.md        (보안 리뷰 기준)
     ├─ performance-policy.md     (성능 리뷰 기준)
     ├─ review-focus.md           (리뷰 우선순위)
     └─ review-ignore.md          (리뷰 제외 항목)
```



**추천!**

하위항목 4개는 필수파일이며, 리뷰 품질이 부족하다고 판단될때 부족한 항목을 하나씩 추가한다.

```
.ai-review/
 ├─ project-profile.md (프로젝트 개요)
 ├─ architecture-rules.md (아키텍처 계층 규칙)
 ├─ class-role-rules.md  (패키지별 역할)
 └─ review-focus.md (리뷰 우선순위)
```



#### project-profile.md 예시

프로젝트 목적, 도메인, 기술 스택, 리뷰 우선순위 설명

```
# Project Profile

## Project Type
- Backend service
- Java / Spring Boot based application
- REST API 중심의 서버 애플리케이션

## Main Responsibility
이 프로젝트는 [도메인명]의 핵심 비즈니스 기능을 제공한다.

예:
- 주문 생성/취소
- 결제 요청/결과 처리
- 회원 권한 검증
- 외부 시스템 연동

## Review Priority
AI 리뷰어는 다음을 우선적으로 검토한다.

1. 비즈니스 규칙이 올바른 계층에 위치하는가
2. Service가 과도한 책임을 가지지 않는가
3. Controller / Service / Repository 책임이 섞이지 않았는가
4. 트랜잭션 경계가 적절한가
5. 예외 처리와 실패 응답이 명확한가
6. 테스트 가능한 구조인가

## Non-goals
다음 항목은 핵심 리뷰 대상이 아니다.

- 단순 포맷팅
- 취향 기반 네이밍 지적
- import 정렬
- 의미 없는 마이크로 최적화
```



#### architecture-rules.md 예시

Controller/Service/Domain/Repository 계층 역할과 아키텍처 규칙

```
# Architecture Rules

## Architecture Style
이 프로젝트는 Layered Architecture 또는 Clean Architecture 원칙을 따른다.

## Layer Rules

### Controller
Controller는 HTTP 요청/응답 처리만 담당한다.

허용:
- Request DTO 수신
- Response DTO 반환
- 인증 사용자 식별 정보 전달
- Service 호출

금지:
- 비즈니스 규칙 판단
- Repository 직접 호출
- Entity 직접 반환
- 복잡한 조건문 처리

### Service
Service는 유스케이스 흐름을 조정한다.

허용:
- 도메인 객체 조회
- 도메인 정책 호출
- 트랜잭션 경계 설정
- 외부 시스템 호출 조합

주의:
- Service가 모든 비즈니스 규칙을 직접 가지면 안 된다.
- 조건문이 많아지면 Domain Service, Policy, Validator 분리를 검토한다.

### Domain / Model
Domain은 핵심 비즈니스 규칙을 표현한다.

허용:
- 상태 변경 규칙
- 유효성 검증
- 계산 로직
- 도메인 예외 발생

금지:
- HTTP, DB, Framework 의존
- Repository 직접 호출

### Repository
Repository는 데이터 접근만 담당한다.

허용:
- Entity 조회/저장
- QueryDSL/MyBatis/JPA 쿼리
- 영속성 관련 로직

금지:
- 비즈니스 정책 판단
- 외부 API 호출
- DTO 조립 중심의 복잡한 유스케이스 처리

## Dependency Direction

허용 방향:

Controller → Service → Domain  
Service → Repository  
Service → External Client

금지 방향:

Domain → Controller  
Domain → Repository  
Repository → Service  
Repository → Controller
```



#### **package-role-map.md 예시**

패키지별 책임과 허용/금지 역할 정의

```
# Package Role Map

## Package Responsibilities

| Package | Role |
|---|---|
| `*.api` | REST Controller, Request/Response DTO |
| `*.service` | 유스케이스 오케스트레이션 |
| `*.domain` | 핵심 도메인 모델, 도메인 규칙 |
| `*.model` | 내부 데이터 모델 |
| `*.entity` | JPA Entity |
| `*.repository` | DB 접근 |
| `*.client` | 외부 API 호출 |
| `*.config` | 설정 클래스 |
| `*.exception` | 도메인/애플리케이션 예외 |
| `*.policy` | 정책/판단 규칙 |
| `*.validator` | 입력/상태 검증 |
| `*.mapper` | DTO / Entity / Model 변환 |
| `*.store` | 인메모리 저장소 또는 캐시성 저장소 |

## Review Rules

- `api` 패키지에서 `repository`를 직접 호출하면 위반이다.
- `entity`가 `api`, `service`, `client` 패키지에 의존하면 위반이다.
- `repository`에서 비즈니스 판단을 하면 위반이다.
- `service`가 너무 많은 도메인 규칙을 직접 판단하면 분리 제안한다.
- `mapper`는 변환만 담당해야 하며 비즈니스 판단을 포함하면 안 된다.
```



#### class-role-rules.md

Controller, Service, Policy, Validator, Mapper 등 클래스 유형별 책임 정의

```
# Class Role Rules

## Controller Class

이름 예:
- `OrderApi`
- `OrderController`

책임:
- 요청 수신
- Request DTO 검증 위임
- Service 호출
- Response DTO 반환

리뷰 포인트:
- Entity를 직접 반환하지 않는가
- Repository를 직접 호출하지 않는가
- 비즈니스 조건문이 많지 않은가

---

## Service Class

이름 예:
- `OrderService`
- `OrderCreateService`
- `PaymentApplyService`

책임:
- 하나의 유스케이스 흐름 처리
- 트랜잭션 경계 관리
- Domain / Policy / Repository 조합

리뷰 포인트:
- 하나의 Service가 너무 많은 유스케이스를 처리하지 않는가
- 메서드가 과도하게 길지 않은가
- 도메인 규칙이 Service 내부 조건문으로 과도하게 퍼져 있지 않은가

---

## Policy Class

이름 예:
- `OrderCancelPolicy`
- `PaymentRetryPolicy`

책임:
- 판단 기준, 임계값, 정책 규칙 표현

리뷰 포인트:
- 정책 변경 시 Service 수정이 최소화되는가
- 조건문이 명확히 캡슐화되어 있는가

---

## Validator Class

이름 예:
- `OrderRequestValidator`
- `PaymentStateValidator`

책임:
- 입력값 또는 상태 검증

리뷰 포인트:
- 검증 실패 시 명확한 예외를 발생시키는가
- Controller에 검증 로직이 흩어져 있지 않은가

---

## Mapper Class

책임:
- DTO, Entity, Model 간 변환

금지:
- Repository 호출
- 외부 API 호출
- 비즈니스 정책 판단
```



#### dependency-rules.md

계층 간 허용/금지 의존 방향 정의

```
# Dependency Rules

## Allowed Dependencies

- Controller may depend on Service.
- Service may depend on Repository, Domain, Policy, Validator, Mapper, External Client.
- Repository may depend on Entity and persistence framework.
- Domain should not depend on Spring, HTTP, DB, or external clients.

## Forbidden Dependencies

- Controller → Repository
- Controller → Entity direct response
- Repository → Service
- Repository → Controller
- Domain → Repository
- Domain → External Client
- Entity → Controller DTO
- Entity → Response DTO

## Review Severity

### Critical
- 순환 의존성 발생
- Domain이 Framework 또는 DB에 직접 의존
- Controller가 Repository 직접 호출

### Major
- Service가 여러 도메인의 책임을 동시에 처리
- Repository에 비즈니스 조건이 포함됨
- Mapper에 비즈니스 판단 포함

### Minor
- 단순 네이밍 불일치
- 작은 중복 코드
```



#### domain-glossary.md

도메인 핵심 용어와 비즈니스 의미 정리

```
# Domain Glossary

## Terms

| Term | Meaning |
|---|---|
| User | 서비스를 사용하는 회원 |
| Order | 사용자가 생성한 주문 |
| Payment | 주문에 대한 결제 요청 또는 결과 |
| Product | 주문 가능한 상품 |
| Cancel | 생성된 주문을 취소하는 행위 |
| Settlement | 결제 이후 정산 처리 |

## Domain Rules

- Order는 Payment 없이 완료 상태가 될 수 없다.
- Payment 실패 시 Order는 결제 완료 상태가 되면 안 된다.
- Cancel은 Order 상태가 완료/배송중일 경우 제한될 수 있다.
- User 권한 검증 없이 타인의 Order를 조회하면 안 된다.

## Naming Rules

- 도메인 용어는 코드에서 일관되게 사용한다.
- `Order`, `Payment`, `Cancel` 같은 핵심 용어를 임의로 다른 단어로 대체하지 않는다.
- 약어 사용은 최소화한다.
```



#### coding-conventions.md

코딩 스타일, 네이밍, 메서드 작성 기준, 공통 개발 규칙

```
# Coding Conventions

## General

- 의미 있는 이름을 사용한다.
- 메서드는 하나의 책임을 가진다.
- null 처리는 명확하게 한다.
- 매직 넘버는 상수 또는 정책 객체로 분리한다.
- 중복 조건문은 Policy 또는 Validator로 분리한다.

## Java / Spring

- 생성자 주입을 사용한다.
- 필드 주입은 사용하지 않는다.
- `@RequiredArgsConstructor` 사용을 선호한다.
- Service public 메서드는 유스케이스 단위로 작성한다.
- DTO와 Entity를 명확히 분리한다.

## Method Rules

리뷰어는 다음을 지적한다.

- 메서드가 너무 길고 여러 책임을 가질 때
- 중첩 if가 깊을 때
- 동일한 조건문이 여러 곳에 반복될 때
- 예외 메시지가 모호할 때
- Optional을 부적절하게 사용할 때

## Do Not Over-review

다음은 강하게 지적하지 않는다.

- 단순 취향 차이의 변수명
- import 순서
- 줄바꿈 스타일
```



#### testing-policy.md

단위 테스트, 서비스 테스트, 예외/경계값 테스트 기준

```
# Testing Policy

## Test Priority

AI 리뷰어는 다음 테스트 누락을 우선적으로 지적한다.

1. 핵심 비즈니스 규칙 테스트 누락
2. 예외 케이스 테스트 누락
3. 경계값 테스트 누락
4. 트랜잭션 실패 케이스 누락
5. 외부 API 실패 케이스 누락

## Unit Test

대상:
- Policy
- Validator
- Domain Model
- 계산 로직
- 상태 변경 로직

## Service Test

대상:
- 유스케이스 흐름
- Repository/Client 조합
- 성공/실패 분기
- 예외 발생 조건

## Controller Test

대상:
- 요청 파라미터 검증
- 응답 상태 코드
- 인증/인가 실패

## Review Rules

- 신규 정책 로직이 추가되면 테스트가 필요하다.
- 버그 수정 PR에는 재발 방지 테스트가 있어야 한다.
- 단순 getter/setter 테스트는 요구하지 않는다.
```



#### error-handling-policy.md

예외 처리 방식, 에러 응답, 실패 처리 기준

```
# Error Handling Policy

## General Rules

- 예외는 의미 있는 커스텀 예외를 우선 사용한다.
- `RuntimeException`을 직접 던지는 것은 지양한다.
- 예외 메시지는 원인과 식별자를 포함한다.
- 예외를 삼키고 로그만 남기면 안 된다.

## Controller Error Response

- 클라이언트에게 내부 구현 상세를 노출하지 않는다.
- 에러 코드는 일관된 형식을 사용한다.
- 4xx와 5xx를 명확히 구분한다.

## Service Error Handling

- 비즈니스 실패는 도메인/애플리케이션 예외로 표현한다.
- 외부 API 실패는 재시도 가능 여부를 구분한다.
- DB 실패를 무조건 일반 예외로 감싸지 않는다.

## Review Rules

리뷰어는 다음을 지적한다.

- `catch (Exception e)` 후 무시
- 모호한 예외 메시지
- 잘못된 HTTP 상태 코드
- 실패했는데 성공 응답 반환
- 외부 API 실패 처리 누락
```



#### transaction-policy.md

트랜잭션 경계, readOnly, 외부 API 호출 시 주의사항

```
# Transaction Policy

## Transaction Boundary

- 트랜잭션은 Service 계층에서 시작한다.
- Controller에서 트랜잭션을 시작하지 않는다.
- Repository 메서드에 불필요한 트랜잭션을 선언하지 않는다.

## Read-only

- 조회 전용 메서드는 `@Transactional(readOnly = true)` 사용을 권장한다.
- 상태 변경 메서드는 일반 `@Transactional`을 사용한다.

## External API

- 외부 API 호출을 긴 DB 트랜잭션 내부에서 수행하지 않는 것을 선호한다.
- 외부 API 호출과 DB 변경이 함께 있을 경우 실패 보상 전략을 검토한다.

## Review Rules

리뷰어는 다음을 지적한다.

- 상태 변경인데 트랜잭션이 없음
- 조회 전용인데 readOnly가 없음
- 트랜잭션 안에서 불필요하게 오래 걸리는 외부 호출 수행
- 여러 저장 작업 중 일부 실패 시 데이터 불일치 가능성
```



#### security-policy.md

인증/인가, 민감정보, 입력값 검증, 보안 리뷰 기준

```
# Security Policy

## Authentication / Authorization

- 인증 사용자 식별은 Controller 또는 Security Context에서 가져온다.
- 권한 검증 없이 타인의 리소스에 접근하면 안 된다.
- 관리자 기능은 명확한 권한 검증이 필요하다.

## Sensitive Data

- 비밀번호, 토큰, 인증키는 로그에 남기지 않는다.
- 개인정보는 필요한 경우에만 응답한다.
- 내부 에러 상세를 클라이언트에게 노출하지 않는다.

## Input Validation

- 외부 입력값은 검증해야 한다.
- 파일명, URL, redirect 파라미터는 신뢰하지 않는다.
- SQL Injection, Path Traversal 가능성을 검토한다.

## Review Rules

리뷰어는 다음을 Critical로 지적한다.

- 인증/인가 누락
- 토큰/비밀번호 로그 출력
- 사용자 입력을 검증 없이 쿼리/파일/URL에 사용
- 내부 시스템 정보 노출
```



#### performance-policy.md

DB 조회, N+1, 페이징, 캐시, 외부 호출 성능 기준

```
# Performance Policy

## Database

- 반복문 내부의 DB 조회를 주의한다.
- N+1 쿼리 가능성을 검토한다.
- 대량 데이터 조회 시 페이징을 사용한다.
- 인덱스가 필요한 조회 조건을 검토한다.

## Memory

- 큰 컬렉션을 불필요하게 메모리에 적재하지 않는다.
- 대용량 파일/응답은 스트리밍을 검토한다.

## External Calls

- 외부 API 호출에는 timeout이 필요하다.
- 반복 호출은 batch 또는 cache를 검토한다.
- 실패 재시도는 무한 반복되면 안 된다.

## Review Rules

리뷰어는 다음을 지적한다.

- 반복문 안의 Repository 호출
- 전체 데이터 조회 후 애플리케이션 필터링
- timeout 없는 외부 API 호출
- 캐시 없이 반복 계산하는 구조
```



#### review-focus.md

AI 코드 리뷰어가 우선적으로 봐야 할 리뷰 항목

```
# Review Focus

AI 리뷰어는 다음 항목을 우선적으로 리뷰한다.

## Priority 1

- 버그 가능성
- 예외/실패 케이스 누락
- 인증/인가 누락
- 데이터 정합성 문제
- 트랜잭션 문제

## Priority 2

- SOLID 위반
- Clean Architecture 위반
- 책임 분리 문제
- 테스트 누락
- 성능 문제

## Priority 3

- 네이밍 개선
- 중복 제거
- 가독성 개선

## Review Style

- 반드시 근거를 설명한다.
- 문제가 없는 코드는 억지로 지적하지 않는다.
- 단순 취향성 리뷰는 피한다.
- 변경 diff와 프로젝트 규칙을 함께 고려한다.
- 심각도는 Critical / Major / Minor / Suggestion 중 하나로 표시한다.
```



#### review-ignore.md

리뷰에서 제외하거나 낮은 우선순위로 볼 항목

```
# Review Ignore

AI 리뷰어는 다음 항목을 기본적으로 리뷰하지 않는다.

## Ignore Files

- `build/`
- `out/`
- `.gradle/`
- `.idea/`
- `node_modules/`
- generated source
- migration snapshot
- lock file

## Ignore Review Topics

- import 정렬
- 단순 줄바꿈
- 포맷터로 해결 가능한 스타일
- 테스트 데이터의 사소한 네이밍
- 로그 문구의 사소한 표현

## Low Priority

다음은 명확한 문제가 있을 때만 리뷰한다.

- 변수명 취향
- 메서드 순서
- 주석 표현
- private 메서드 위치
```





# 서버 측 필요 MD



```
i-code-reviewer-server/
 ├─ src/
 ├─ build.gradle
 ├─ README.md
 └─ docs/
     ├─ service-profile.md              (AI 코드 리뷰어 서버의 목적, 범위, 주요 기능, 비기능 요구사항) v
     ├─ review-pipeline.md              (GitHub Webhook 수신부터 PR 댓글 게시까지의 전체 리뷰 처리 흐름) v
     ├─ context-collection-policy.md    (PR 리뷰에 필요한 diff, 변경 파일 원문, README, .ai-review 문서 수집 기준) v
     ├─ prompt-policy.md                (LLM에게 전달할 시스템/유저 프롬프트 구성 원칙과 금지사항) v
     ├─ review-output-format.md         (AI 리뷰 결과의 Markdown 형식, 섹션 구조, 코드 위치 표시 방식) v
     ├─ comment-publishing-policy.md    (PR 코멘트 작성 위치, 중복 댓글 방지, 업데이트/재게시 정책) v
     ├─ severity-policy.md              (BLOCKER, MAJOR, MINOR, SUGGESTION 등 심각도 분류 기준) v
     ├─ privacy-security-policy.md      (토큰, 소스코드, 로그, 민감정보 보호 및 외부 전송 제한 정책)v
     ├─ failure-handling-policy.md      (GitHub API 실패, LLM 실패, RAG 실패, timeout 발생 시 처리 방식) v
     ├─ github-integration-policy.md    (GitHub App/PAT 권한, Webhook 이벤트, API 호출 범위 정의) v
     ├─ token-budget-policy.md          (LLM 컨텍스트 길이 제한에 맞춘 문서 압축, 우선순위, 제외 기준) v 
     ├─ review-scope-policy.md          (diff 리뷰, 파일 전체 리뷰, 아키텍처 리뷰, 보안 리뷰의 범위 구분) v
     ├─ test-strategy.md                (Webhook, GitHub API client, RAG, Prompt 생성, 리뷰 파이프라인 테스트 전략) v
     └─ local-development-guide.md      (로컬 실행, 환경변수, Ollama/OpenAI 설정, 테스트용 Webhook 실행 방법) v
```







#### service-profile.md

```
service-profile.md
Role

ai-code-reviewer-server는 GitHub Pull Request 이벤트를 받아 변경 코드를 수집하고, LLM으로 코드 리뷰를 생성한 뒤 PR 댓글로 게시하는 서버이다.

이 서버는 단순 diff 리뷰어가 아니라, 클라이언트 프로젝트의 .ai-review/ 문서를 함께 읽고 프로젝트 문맥 기반 리뷰를 수행한다.

Core Responsibilities
GitHub Webhook을 수신한다.
Pull Request 이벤트만 리뷰 대상으로 처리한다.
PR diff, 변경 파일 원문, 관련 문서를 GitHub API로 수집한다.
클라이언트 프로젝트의 .ai-review/ 문서를 리뷰 기준으로 사용한다.
필요한 경우 RAG에서 프로젝트 문맥을 검색한다.
LLM 프롬프트를 생성한다.
LLM 리뷰 결과를 Markdown 형식으로 정리한다.
GitHub PR 댓글로 리뷰 결과를 게시한다.
실패 원인과 처리 결과를 로그로 남긴다.
Non-Responsibilities
소스코드를 직접 수정하지 않는다.
PR을 자동 merge하지 않는다.
빌드와 테스트를 직접 실행하지 않는다.
클라이언트 프로젝트의 아키텍처 규칙을 서버 코드에 하드코딩하지 않는다.
수집하지 않은 코드나 문서를 근거로 리뷰하지 않는다.
존재하지 않는 파일, 클래스, 정책을 만들어내지 않는다.
Review Principle

리뷰는 항상 현재 PR 변경사항을 중심으로 수행한다.

우선순위는 다음과 같다.

실제 diff
변경 파일 전체 원문
.ai-review/ 프로젝트 규칙
관련 테스트 파일
README와 빌드 파일
RAG 검색 결과

LLM은 제공된 컨텍스트 안에서만 판단해야 한다.
근거가 부족하면 단정하지 말고 확인 필요로 표시해야 한다.

Output Rule

리뷰 결과는 PR 작성자가 바로 수정할 수 있어야 한다.

가능하면 다음 정보를 포함한다.

문제 위치
심각도
문제 이유
수정 방향
근거가 된 코드 또는 정책
```



#### review-pipeline.md

```
review-pipeline.md
Pipeline

PR 리뷰 파이프라인은 다음 순서로 동작한다.

GitHub Webhook 수신
Signature 검증
Pull Request 이벤트 필터링
PR 메타데이터 추출
변경 파일과 diff 수집
변경 파일 원문 수집
.ai-review/ 문서 수집 또는 RAG 검색
리뷰 컨텍스트 구성
LLM 프롬프트 생성
모델 선택
LLM 리뷰 생성
리뷰 결과 검증
Markdown 댓글 포맷팅
GitHub PR 댓글 게시
결과 로깅
Event Rules

처리 대상 action은 다음으로 제한한다.

opened
synchronize
reopened

그 외 action은 로그만 남기고 리뷰하지 않는다.

Required Metadata

PR 처리에는 다음 정보가 필요하다.

owner
repository
pull request number
base branch
head branch
commit sha
PR title
PR body
changed files
Failure Rule
Signature 검증 실패 시 즉시 종료한다.
GitHub API 전체 실패 시 리뷰를 중단한다.
일부 문서 수집 실패는 경고로 남기고 가능한 범위에서 리뷰한다.
LLM 실패 시 PR에 부정확한 리뷰를 게시하지 않는다.
댓글 게시 실패는 로그로 남기고 재시도 가능하게 설계한다.
Design Rule

각 단계는 독립된 책임으로 분리한다.

권장 구조:

WebhookController
PullRequestReviewService
GithubClient
ContextCollector
RagRetrievalService
PromptBuilder
AiReviewService
ReviewCommentPublisher
```



#### context-collection-policy.md

```
context-collection-policy.md
Purpose

PR 리뷰에 필요한 코드와 문서를 수집하는 기준을 정의한다.

Required Context

항상 우선 수집한다.

PR metadata
changed files
file patch / diff
변경 파일 전체 원문
.ai-review/ 핵심 문서
README.md
build.gradle 또는 pom.xml
Optional Context

필요할 때만 수집한다.

변경 파일의 관련 테스트 파일
변경 클래스가 구현하는 interface
변경 클래스가 상속하는 parent class
같은 package의 주요 연관 클래스
프로젝트 디렉터리 구조 요약
Priority

컨텍스트 우선순위는 다음과 같다.

PR diff
변경 파일 patch
변경 파일 전체 원문
.ai-review/project-profile.md
.ai-review/architecture-rules.md
.ai-review/package-role-map.md
.ai-review/class-role-rules.md
.ai-review/review-focus.md
.ai-review/review-ignore.md
테스트 파일
빌드 파일
README
Exclude

다음은 기본 수집하지 않는다.

.git/
.gradle/
.idea/
build/
target/
out/
node_modules/
binary file
image file
generated file
lock file
secret file

민감정보 파일은 수집하지 않는다.

예시:

.env
application-prod.yml
secrets.yml
private-key.pem
*.key
*.p12
*.jks
Rule

수집되지 않은 파일을 근거로 리뷰하지 않는다.
컨텍스트가 부족하면 확인 필요로 표시한다.
```



#### rag-ingestion-policy.md

```
rag-ingestion-policy.md
Purpose

반복적으로 참조되는 프로젝트 문서를 RAG 지식베이스에 적재하는 기준을 정의한다.

Ingestion Targets

RAG 적재 대상은 재사용 가능한 프로젝트 문서이다.

.ai-review/project-profile.md
.ai-review/architecture-rules.md
.ai-review/package-role-map.md
.ai-review/class-role-rules.md
.ai-review/dependency-rules.md
.ai-review/coding-conventions.md
.ai-review/testing-policy.md
.ai-review/error-handling-policy.md
.ai-review/transaction-policy.md
.ai-review/security-policy.md
.ai-review/performance-policy.md
.ai-review/review-focus.md
.ai-review/review-ignore.md
README.md
Non-Ingestion Targets

다음은 RAG에 적재하지 않는다.

PR diff
PR patch
일회성 리뷰 결과
GitHub token
secret file
binary file
build output
source code 전체

소스코드는 기본적으로 PR 리뷰 시점에 직접 수집한다.

Metadata

각 chunk에는 다음 metadata를 포함한다.

tenantId
owner
repository
branch
commitSha
filePath
documentType
chunkIndex
contentHash
ingestedAt
Chunking Rule
Markdown heading 기준으로 나눈다.
하나의 chunk는 하나의 주제만 담는다.
너무 작은 chunk는 병합한다.
너무 큰 chunk는 문단 단위로 분리한다.
코드 블록은 가능하면 분리하지 않는다.

권장 크기:

target: 2,000 ~ 4,000 characters
max: 6,000 characters
overlap: 300 ~ 500 characters
Update Rule

같은 파일의 contentHash가 바뀌면 기존 chunk를 삭제하고 다시 적재한다.
삭제된 파일의 chunk는 제거한다.

MVP Scope

MVP에서는 다음만 적재한다.

project-profile
architecture-rules
package-role-map
class-role-rules
review-focus
review-ignore
README
```



#### retrieval-policy.md

```
retrieval-policy.md
Purpose

PR 리뷰 시 RAG에서 어떤 문서를 검색하고 프롬프트에 넣을지 정의한다.

Basic Rule

Retrieval은 현재 PR diff와 변경 파일을 기준으로 수행한다.
RAG 결과는 diff를 대체하지 않는다.

Required Filter

검색은 반드시 현재 프로젝트 범위로 제한한다.

tenantId
owner
repository
branch 또는 default branch

다른 프로젝트 문서를 검색 결과에 포함하지 않는다.

Always Include

가능하면 다음 문서는 우선 포함한다.

.ai-review/project-profile.md
.ai-review/review-focus.md
.ai-review/review-ignore.md
Conditional Retrieval

변경 내용에 따라 다음 문서를 검색한다.

아키텍처 변경 → architecture-rules.md, package-role-map.md, class-role-rules.md
의존성 변경 → dependency-rules.md
테스트 관련 변경 → testing-policy.md
예외 처리 변경 → error-handling-policy.md
트랜잭션 변경 → transaction-policy.md
보안 관련 변경 → security-policy.md
성능 관련 변경 → performance-policy.md
코딩 스타일 변경 → coding-conventions.md
Query Inputs

검색 query는 다음 정보를 조합한다.

changed file path
package name
class name
layer name
PR title
diff summary
review category
Limit

MVP에서는 최대 10개 chunk 이내로 제한한다.

우선순위:

변경 파일과 직접 관련된 chunk
class-role-rules
package-role-map
architecture-rules
review-focus
README
Fallback

RAG 검색 결과가 없더라도 리뷰는 계속한다.
이 경우 diff와 변경 파일 원문 기준으로 리뷰한다.
```



#### prompt-policy.md

```
prompt-policy.md
Purpose

LLM에게 전달할 프롬프트 구성 원칙을 정의한다.

Prompt Structure

프롬프트는 다음 순서로 구성한다.

역할
리뷰 목표
금지사항
PR 메타데이터
변경 파일 목록
diff
변경 파일 원문
.ai-review 규칙
RAG 검색 결과
출력 형식
Required Instructions

LLM에게 반드시 지시한다.

제공된 컨텍스트만 근거로 리뷰한다.
존재하지 않는 파일, 클래스, 정책을 언급하지 않는다.
확실하지 않으면 확인 필요로 표시한다.
단순 취향성 리뷰는 피한다.
실제 수정 가능한 제안을 한다.
심각도와 근거를 함께 제시한다.
PR 변경사항과 직접 관련 없는 장황한 설명은 피한다.
Context Priority

프롬프트에 넣는 우선순위는 다음과 같다.

diff
변경 파일 원문
.ai-review 핵심 규칙
관련 테스트
RAG 검색 결과
README
빌드 파일
Do Not
전체 repository를 무조건 넣지 않는다.
중복 문서를 반복 주입하지 않는다.
민감정보를 포함하지 않는다.
서버 내부 token이나 환경변수를 포함하지 않는다.
LLM에게 추측을 요구하지 않는다.
Output Constraint

리뷰 결과는 Markdown으로 작성한다.
심각도는 BLOCKER, MAJOR, MINOR, SUGGESTION 중 하나를 사용한다.
```



#### model-routing-policy.md

```
model-routing-policy.md
Purpose

리뷰 요청의 크기와 성격에 따라 사용할 LLM 모델을 선택하는 기준을 정의한다.

Routing Inputs

모델 선택 시 다음 정보를 사용한다.

diff size
changed file count
context size
review category
architecture review 여부
security review 여부
expected latency
model cost
model availability
Basic Rule
작은 diff는 빠른 모델을 사용한다.
큰 diff는 긴 컨텍스트 모델을 사용한다.
아키텍처 리뷰는 추론 성능이 좋은 모델을 사용한다.
보안 리뷰는 정확도와 안정성이 높은 모델을 사용한다.
모델 장애 시 fallback 모델을 사용한다.
Suggested Routing
small diff → fast model
medium diff → balanced model
large diff → long-context model
architecture review → reasoning model
security review → high-accuracy model
fallback → local model or default model
Do Not
모든 요청에 가장 비싼 모델을 사용하지 않는다.
컨텍스트가 큰 요청을 짧은 컨텍스트 모델에 넣지 않는다.
모델 실패 응답을 그대로 PR에 게시하지 않는다.
모델 선택 기준을 비즈니스 코드에 흩뿌리지 않는다.
Output

모델 라우팅 결과에는 다음을 포함한다.

selectedModel
reason
fallbackModel
estimatedContextSize
```



#### review-output-format.md

```
review-output-format.md
Purpose

AI 코드 리뷰 결과의 Markdown 출력 형식을 정의한다.

Required Format

리뷰 댓글은 다음 구조를 따른다.

AI Code Review
Summary

PR 변경사항에 대한 전체 요약을 작성한다.

Findings

각 이슈는 다음 형식을 사용한다.

[SEVERITY] 제목
File: path/to/File.java
Location: 가능하면 class, method, line 정보를 작성
Reason: 왜 문제인지 설명
Suggestion: 어떻게 수정할지 제안
Check Needed

근거가 부족하지만 확인이 필요한 항목을 작성한다.

Good Points

필요한 경우 긍정적인 변경사항을 짧게 작성한다.

Severity

허용되는 심각도는 다음뿐이다.

BLOCKER
MAJOR
MINOR
SUGGESTION
Rules
이슈가 없으면 억지로 만들지 않는다.
파일 경로는 실제 변경 파일 또는 제공된 컨텍스트에 있는 파일만 사용한다.
코드 전체를 길게 복사하지 않는다.
수정 제안은 구체적으로 작성한다.
PR 작성자가 바로 행동할 수 있어야 한다.
No Issue Format

문제가 없으면 다음처럼 작성한다.

이번 PR에서 제공된 컨텍스트 기준으로 명확한 문제는 발견하지 못했습니다.
```



#### comment-publishing-policy.md

```
comment-publishing-policy.md
Purpose

AI 리뷰 결과를 GitHub PR 댓글로 게시하는 기준을 정의한다.

Publishing Rule
리뷰 결과는 PR conversation comment로 게시한다.
동일 commit에 대해 중복 댓글을 반복 게시하지 않는다.
가능하면 댓글에 commit sha 또는 review fingerprint를 포함한다.
LLM 응답이 비어 있으면 댓글을 게시하지 않는다.
검증 실패한 응답은 게시하지 않는다.
Duplicate Prevention

중복 방지를 위해 다음 값을 사용한다.

owner
repository
pull request number
commit sha
review fingerprint

동일 fingerprint가 이미 게시되었다면 새 댓글을 만들지 않는다.

Update Rule

MVP에서는 기존 댓글 수정 대신 새 commit에 대해서만 새 리뷰 댓글을 작성한다.

향후 확장 시 bot이 작성한 기존 댓글을 찾아 update할 수 있다.

Failure Rule
GitHub API 401/403 → 권한 문제로 기록하고 중단한다.
GitHub API 404 → repository 또는 PR 정보를 확인한다.
rate limit → 재시도 또는 큐잉한다.
게시 실패 시 LLM을 다시 호출하지 않는다.
Do Not
민감정보를 댓글에 게시하지 않는다.
내부 stacktrace를 PR 댓글에 게시하지 않는다.
서버 내부 설정값을 댓글에 포함하지 않는다.
실패한 리뷰를 성공처럼 게시하지 않는다.
```



#### severity-policy.md

```
severity-policy.md
Purpose

리뷰 이슈의 심각도 분류 기준을 정의한다.

Severity Levels
BLOCKER

merge 전에 반드시 수정해야 하는 문제.

예시:

실제 장애 가능성이 높은 버그
보안 취약점
데이터 손상 가능성
인증/인가 우회
트랜잭션 오류로 인한 정합성 문제
컴파일 실패 가능성이 명확한 코드
MAJOR

수정이 강하게 권장되는 중요한 문제.

예시:

예외 처리 누락
null 처리 누락
계층 의존성 위반
핵심 비즈니스 규칙 위반
테스트가 필요한 주요 로직 변경
성능 저하 가능성이 큰 코드
MINOR

품질 개선이 필요한 문제.

예시:

중복 코드
네이밍 혼란
책임이 약간 모호한 코드
가독성 저하
작은 테스트 보완 필요
SUGGESTION

선택적 개선 제안.

예시:

더 명확한 메서드명
구조 개선 아이디어
리팩토링 제안
문서 보완
향후 확장성 개선
Rule

심각도는 과장하지 않는다.
근거가 부족하면 확인 필요로 분류하거나 낮은 심각도를 사용한다.
```

#### 

#### hallucination-guard-policy.md

```
hallucination-guard-policy.md
Purpose

LLM이 존재하지 않는 코드, 정책, 파일을 만들어내지 않도록 제한한다.

Rules

LLM은 다음을 반드시 지킨다.

제공된 컨텍스트에 없는 파일을 언급하지 않는다.
제공된 컨텍스트에 없는 클래스나 메서드를 언급하지 않는다.
.ai-review에 없는 프로젝트 정책을 있다고 말하지 않는다.
추측을 사실처럼 말하지 않는다.
diff와 무관한 일반론을 장황하게 작성하지 않는다.
코드 실행 결과를 본 것처럼 말하지 않는다.
테스트를 실행하지 않았으면 실행했다고 말하지 않는다.
빌드를 수행하지 않았으면 성공/실패를 단정하지 않는다.
Uncertainty Rule

근거가 부족하면 다음 표현을 사용한다.

확인 필요
제공된 컨텍스트만으로는 단정하기 어렵습니다
관련 코드가 추가로 필요합니다
Evidence Rule

모든 주요 지적은 다음 중 하나에 근거해야 한다.

PR diff
변경 파일 원문
.ai-review 문서
README
빌드 파일
관련 테스트 파일
RAG 검색 결과
Do Not
없는 라인 번호를 만들어내지 않는다.
없는 아키텍처 규칙을 적용하지 않는다.
일반적인 베스트 프랙티스를 프로젝트 규칙처럼 말하지 않는다.
불확실한 내용을 BLOCKER로 분류하지 않는다.
```



#### multi-tenant-policy.md

```
multi-tenant-policy.md
Purpose

여러 클라이언트 프로젝트와 조직을 안전하게 분리해 처리하는 기준을 정의한다.

Tenant Rule

모든 리뷰 요청은 tenant 기준으로 분리한다.

tenant 식별에는 다음 정보를 사용할 수 있다.

tenantId
GitHub owner
repository
installationId
organization
Data Isolation

다음 데이터는 tenant별로 분리해야 한다.

GitHub token
repository metadata
.ai-review 문서
RAG vector data
cache data
review history
logs
rate limit state
Retrieval Rule

RAG 검색 시 반드시 tenant, owner, repository 필터를 적용한다.

다른 tenant의 문서가 검색 결과에 포함되면 안 된다.

Cache Rule

캐시 key에는 tenant 식별자를 포함한다.

예시:

tenantId:owner:repo:branch:filePath:contentHash

Do Not
tenant 간 문서를 공유하지 않는다.
tenant 간 GitHub token을 재사용하지 않는다.
tenant 필터 없이 RAG 검색하지 않는다.
로그에 다른 tenant의 정보를 섞지 않는다.
```

#### privacy-security-policy.md

```
privacy-security-policy.md
Purpose

소스코드, token, secret, 로그, 외부 LLM 전송과 관련된 보안 기준을 정의한다.

Secret Rule

다음 정보는 수집, 저장, 로그 출력, LLM 전송을 피한다.

GitHub token
API key
password
private key
database credential
access token
refresh token
secret file
production config
Excluded Files

기본 제외 파일:

.env
.env.*
application-prod.yml
application-prod.properties
secrets.yml
private-key.pem
*.key
*.p12
*.jks
Logging Rule

로그에 남겨도 되는 것:

owner
repository
pull request number
changed file count
model name
duration
success/failure status

로그에 남기면 안 되는 것:

token
secret
full source code
full prompt
private key
credential
External LLM Rule

외부 LLM 사용 시 민감정보가 포함된 컨텍스트를 보내지 않는다.
필요하면 마스킹 후 전송한다.

Do Not
민감정보를 PR 댓글에 포함하지 않는다.
내부 stacktrace를 외부로 노출하지 않는다.
모든 소스코드를 무조건 저장하지 않는다.
리뷰 목적 외로 수집 데이터를 사용하지 않는다.
```



#### failure-handling-policy.md

```
failure-handling-policy.md
Purpose

GitHub API, LLM, RAG, timeout 등 실패 상황 처리 기준을 정의한다.

Failure Rules
실패는 숨기지 않고 로그로 남긴다.
복구 가능한 실패는 fallback 처리한다.
부정확한 리뷰를 생성할 위험이 있으면 댓글을 게시하지 않는다.
일부 컨텍스트 실패는 경고로 남기고 리뷰를 계속할 수 있다.
핵심 데이터 수집 실패는 리뷰를 중단한다.
Stop Conditions

다음 경우 리뷰를 중단한다.

signature 검증 실패
PR metadata 추출 실패
changed files 수집 실패
GitHub 인증 실패
LLM 응답 생성 실패
LLM 응답이 비어 있음
리뷰 결과 검증 실패
Continue Conditions

다음 경우 리뷰를 계속할 수 있다.

README 없음
.ai-review 없음
일부 관련 파일 수집 실패
RAG 검색 실패
테스트 파일 없음
일부 optional context 누락
Fallback
RAG 실패 → diff와 파일 원문으로 리뷰
.ai-review 없음 → 일반 Java/Spring 기준으로 리뷰
변경 파일 원문 일부 실패 → diff 기준 제한 리뷰
댓글 게시 실패 → 로그 기록 후 재시도 가능 상태로 남김
Do Not
실패한 LLM 응답을 그대로 게시하지 않는다.
실패 원인에 token이나 secret을 포함하지 않는다.
실패를 성공처럼 기록하지 않는다.
```



#### observability-policy.md

```
observability-policy.md
Purpose

리뷰 요청의 성공, 실패, 지연, 비용을 추적하기 위한 로깅/모니터링 기준을 정의한다.

Required Logs

각 리뷰 요청에는 다음 정보를 남긴다.

requestId
tenantId
owner
repository
pull request number
action
commit sha
changed file count
collected context count
selected model
LLM duration
GitHub API duration
total duration
publish success 여부
failure reason
Metrics

수집할 주요 metric:

webhook received count
review success count
review failure count
GitHub API failure count
LLM failure count
RAG failure count
comment publish failure count
average review duration
token usage
model usage count
Trace Rule

하나의 PR 리뷰 요청은 같은 requestId로 추적한다.

requestId는 다음 단계에 전달한다.

webhook
context collection
RAG retrieval
prompt generation
LLM call
comment publishing
Do Not
full source code를 로그에 남기지 않는다.
full prompt를 로그에 남기지 않는다.
token, secret, credential을 로그에 남기지 않는다.
민감한 LLM 응답 원문을 무조건 저장하지 않는다.
```



#### client-onboarding-guide.md

```
client-onboarding-guide.md
Purpose

신규 클라이언트 프로젝트가 AI 코드 리뷰를 사용하기 위해 준비해야 할 항목을 정의한다.

Required Setup

클라이언트 프로젝트는 가능하면 루트에 .ai-review/ 디렉터리를 둔다.

MVP 필수 문서:

.ai-review/project-profile.md
.ai-review/architecture-rules.md
.ai-review/package-role-map.md
.ai-review/class-role-rules.md
.ai-review/review-focus.md
.ai-review/review-ignore.md
Recommended Documents

추가 권장 문서:

.ai-review/dependency-rules.md
.ai-review/coding-conventions.md
.ai-review/testing-policy.md
.ai-review/error-handling-policy.md
.ai-review/transaction-policy.md
.ai-review/security-policy.md
.ai-review/performance-policy.md
GitHub Setup

필요한 설정:

Webhook URL 등록
Pull Request 이벤트 활성화
secret 설정
GitHub App 또는 PAT 권한 설정
Minimum Repository Files

리뷰 품질 향상을 위해 다음 파일이 있으면 좋다.

README.md
build.gradle 또는 pom.xml
테스트 코드
패키지 구조가 명확한 source directory
Rule

.ai-review 문서는 프로젝트별 리뷰 기준이다.
서버 docs/는 리뷰 서버의 동작 기준이다.
두 역할을 섞지 않는다.

github-integration-policy.md
Purpose

GitHub Webhook, API, 권한 범위를 정의한다.

Webhook Events

처리 대상:

pull_request

처리 action:

opened
synchronize
reopened

무시 action:

closed
edited
assigned
labeled
unlabeled
Required Headers
X-GitHub-Event
X-Hub-Signature-256
request body
Required API Access

필요한 GitHub API 기능:

PR metadata 조회
PR changed files 조회
repository file content 조회
PR comment 작성
기존 bot comment 조회
Required Permissions

GitHub App 기준:

Pull requests: read
Contents: read
Issues: write
Metadata: read

PAT 기준:

repository contents read
pull request read
issue comment write
Rule

Webhook secret 검증 실패 시 처리하지 않는다.
GitHub token은 로그에 남기지 않는다.
```



#### token-budget-policy.md

```
token-budget-policy.md
Purpose

LLM 컨텍스트 길이 제한에 맞춰 어떤 정보를 포함하고 제외할지 정의한다.

Priority

컨텍스트 우선순위:

PR diff
변경 파일 patch
변경 파일 전체 원문
.ai-review 핵심 문서
관련 테스트 파일
RAG 검색 결과
README
빌드 파일
디렉터리 구조
Compression Rule

컨텍스트가 크면 다음 순서로 줄인다.

README 요약
빌드 파일 요약
디렉터리 구조 요약
관련 파일 수 제한
RAG chunk 수 제한
변경 파일 원문 일부 제거
diff는 마지막까지 유지
Limits

MVP 권장 제한:

RAG chunk 최대 10개
관련 소스 파일 최대 5개
관련 테스트 파일 최대 5개
README는 필요한 섹션만 포함
대형 파일은 요약 또는 제외
Do Not
diff보다 README를 우선하지 않는다.
중복 문서를 반복 삽입하지 않는다.
모든 파일 원문을 무조건 넣지 않는다.
컨텍스트 초과 상태로 LLM을 호출하지 않는다.
```



#### review-scope-policy.md

```
review-scope-policy.md
Purpose

리뷰 범위를 diff 리뷰, 파일 전체 리뷰, 아키텍처 리뷰, 보안 리뷰로 구분한다.

Diff Review

항상 수행한다.

대상:

변경 라인
추가/삭제 코드
조건문 변경
예외 처리 변경
API 변경
Full File Review

필요할 때 수행한다.

조건:

변경 라인만으로 판단이 어려움
클래스 책임 확인 필요
필드, 생성자, 의존성 확인 필요
메서드 전체 흐름 확인 필요
Architecture Review

다음 경우 수행한다.

새 클래스 추가
패키지 이동
계층 간 의존성 변경
Service/Controller/Repository 변경
interface/adapter 변경
.ai-review 아키텍처 문서 존재
Security Review

다음 경우 수행한다.

인증/인가 변경
token/password/secret 처리 변경
외부 입력 처리 변경
SQL/동적 쿼리 변경
파일 업로드/다운로드 변경
로그 출력 변경
Rule

리뷰 범위를 명확히 구분한다.
범위 밖의 내용을 억지로 리뷰하지 않는다.
```



#### cache-policy.md

```
cache-policy.md
Purpose

GitHub API 호출과 RAG 비용을 줄이기 위한 캐싱 기준을 정의한다.

Cache Targets

캐시 대상:

README.md
.ai-review 문서
build.gradle 또는 pom.xml
변경 파일 원문
RAG embedding 결과
directory structure summary
Cache Key

캐시 key에는 다음을 포함한다.

tenantId
owner
repository
branch
commitSha 또는 contentHash
filePath
Invalidation

다음 경우 캐시를 무효화한다.

contentHash 변경
commitSha 변경
branch 변경
.ai-review 문서 변경
수동 재적재 요청
Do Not
GitHub token을 캐시하지 않는다.
secret 파일을 캐시하지 않는다.
tenant 구분 없는 key를 사용하지 않는다.
오래된 문서로 현재 PR을 단정하지 않는다.
```



#### rate-limit-policy.md

```
rate-limit-policy.md
Purpose

GitHub API, LLM API, 동시 리뷰 요청의 rate limit 대응 기준을 정의한다.

Limits

제어 대상:

GitHub API 호출 수
LLM 호출 수
동시 PR 리뷰 수
repository별 리뷰 요청 수
tenant별 리뷰 요청 수
Rules
동일 PR의 동일 commit은 중복 리뷰하지 않는다.
동시에 너무 많은 LLM 요청을 보내지 않는다.
GitHub rate limit 응답을 받으면 재시도하거나 큐잉한다.
대형 PR은 context 수집과 LLM 호출을 제한한다.
tenant별 사용량을 추적한다.
Retry

재시도 가능:

일시적 네트워크 오류
429 rate limit
5xx API 오류

재시도하지 않음:

401
403 권한 없음
404 repository 또는 PR 없음
signature 검증 실패
Do Not
실패한 요청을 무한 재시도하지 않는다.
rate limit 상태에서 LLM 호출을 반복하지 않는다.
같은 PR에 중복 댓글을 만들지 않는다.
```



#### test-strategy.md

```
test-strategy.md
Purpose

AI 코드 리뷰어 서버의 핵심 기능 테스트 기준을 정의한다.

Test Targets

우선 테스트 대상:

GitHub Webhook 검증
Pull Request 이벤트 필터링
GitHub API client
ContextCollector
RAG ingestion
RetrievalService
PromptBuilder
ModelRouter
AiReviewService
CommentPublisher
Failure handling
Unit Test

단위 테스트는 다음을 검증한다.

지원 action만 처리하는지
metadata를 정확히 추출하는지
제외 파일을 수집하지 않는지
prompt에 필수 규칙이 포함되는지
severity가 올바르게 매핑되는지
중복 댓글 방지 key가 생성되는지
Integration Test

통합 테스트는 다음 흐름을 검증한다.

webhook payload 입력
PR 파일 수집 mock
context 구성
LLM 응답 mock
PR 댓글 게시 mock
Do Not
실제 GitHub API에 의존하는 테스트를 기본 테스트로 만들지 않는다.
실제 LLM 응답에 의존하는 테스트를 만들지 않는다.
secret이 필요한 테스트를 기본 실행에 포함하지 않는다.
MVP

MVP에서는 mock 기반 파이프라인 테스트를 우선 작성한다.
```









```
ai:
  review:
    model: qwen3-coder:30b
    fallback-model: qwen3:30b
    fast-model: qwen3.5:9b
    
1단계 MVP 개발:
qwen3:30b 사용

2단계 코드 리뷰 품질 검증:
qwen3-coder:30b 추가 다운로드

3단계 비교 테스트:
같은 PR diff를 qwen3:30b / qwen3-coder:30b / qwen3.5:27b로 비교
```

