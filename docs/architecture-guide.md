# Architecture Guide

## Layer Architecture

프로젝트는 아래 구조를 따른다.

api
↓
Service
↓
Repository

---

## Controller 규칙

api는 비즈니스 로직을 포함하지 않는다.

허용

- Request 검증
- DTO 변환
- Service 호출

금지

- DB 접근
- 복잡한 계산
- 트랜잭션 처리

---

## Service 규칙

비즈니스 로직은 Service에 작성한다.

Service는

- 트랜잭션 경계
- 도메인 규칙
- 외부 시스템 호출

을 담당한다.

---

## Repository 규칙

Repository는 데이터 접근만 담당한다.

비즈니스 로직 작성 금지

---

## Event 규칙

비동기 이벤트는 EventPublisher 사용

Service 간 직접 호출보다 이벤트를 우선 고려한다.

---

## Exception 규칙

비즈니스 예외는 CustomException 사용

Exception swallow 금지

예시

잘못된 코드

try {
...
} catch(Exception ignored) {
}

권장

try {
...
} catch(Exception e) {
throw new CustomException(...)
}