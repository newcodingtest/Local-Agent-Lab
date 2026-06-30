# Review Scope Policy

## Purpose

AI 코드 리뷰어가 Pull Request에서 어떤 범위까지 리뷰해야 하는지 정의한다.

이 문서의 목적은 리뷰 범위를 명확히 제한하여 다음 문제를 방지하는 것이다.

- PR 변경사항과 무관한 과잉 리뷰
- 전체 프로젝트를 본 것처럼 단정하는 리뷰
- Clean Architecture 위반을 과장하는 리뷰
- 빌드/테스트를 실행한 것처럼 말하는 리뷰
- 제공되지 않은 코드까지 추측하는 리뷰

## Basic Rule

리뷰는 항상 현재 Pull Request 변경사항을 중심으로 수행한다.

우선순위는 다음과 같다.

1. PR diff
2. 변경 파일 전체 원문
3. 변경 파일과 직접 관련된 파일
4. 관련 테스트 파일
5. `.ai-review/` 프로젝트 규칙
6. README / build 파일

수집되지 않은 파일이나 문서를 근거로 리뷰하지 않는다.

## Review Scope Types

AI 리뷰어는 다음 범위를 구분해서 리뷰한다.

- Diff Review
- Full File Review
- Project Rule Review
- Architecture Review
- Test Review
- Security Review
- Performance Review

## Diff Review

Diff Review는 항상 수행한다.

대상은 다음과 같다.

- 추가된 코드
- 삭제된 코드
- 수정된 코드
- 조건문 변경
- 예외 처리 변경
- 외부 API 호출 변경
- DB 접근 로직 변경
- 트랜잭션 변경
- 테스트 추가/삭제/수정

Diff에서 확인 가능한 문제는 명확히 지적할 수 있다.

## Full File Review

변경 파일 전체 원문이 제공된 경우 수행한다.

확인 대상은 다음과 같다.

- 변경 메서드의 주변 문맥
- 클래스의 기존 책임
- 필드와 생성자 의존성
- private method 흐름
- 변경 코드가 기존 클래스 책임과 맞는지 여부
- 변경 코드가 기존 예외 처리 방식과 맞는지 여부

단, 변경되지 않은 코드 자체를 별도 이슈로 과도하게 지적하지 않는다.  
변경사항과 직접 연결될 때만 리뷰한다.

## Project Rule Review

`.ai-review/` 문서가 제공된 경우 해당 프로젝트 규칙을 리뷰 기준으로 사용한다.

우선 적용할 문서는 다음과 같다.

```text
.ai-review/project-profile.md
.ai-review/architecture-rules.md
.ai-review/package-role-map.md
.ai-review/class-role-rules.md
.ai-review/dependency-rules.md
.ai-review/testing-policy.md
.ai-review/error-handling-policy.md
.ai-review/transaction-policy.md
.ai-review/security-policy.md
.ai-review/performance-policy.md
.ai-review/review-focus.md
.ai-review/review-ignore.md
```

프로젝트 규칙이 제공되지 않은 경우 일반 Java/Spring, Clean Code 기준으로 제한적으로 리뷰한다.

프로젝트 규칙이 없는 내용을 프로젝트 정책인 것처럼 말하지 않는다.

## Architecture Review

Architecture Review는 다음 경우에만 수행한다.

- 새 클래스가 추가됨
- 패키지 위치가 변경됨
- Controller, Service, Repository, Domain, Adapter 계층 코드가 변경됨
- 계층 간 호출 구조가 변경됨
- 외부 API, DB, 메시징, 파일 시스템 의존성이 핵심 로직에 추가됨
- interface, port, adapter 구조가 변경됨
- **.ai-review/architecture-rules.md** 또는 관련 문서가 제공됨

Architecture Review에서 확인할 항목은 다음과 같다.

- 책임 분리
- 계층 간 의존 방향
- 도메인 로직과 인프라 세부사항의 분리
- Controller의 비즈니스 로직 포함 여부
- Repository의 비즈니스 규칙 포함 여부
- Application Service의 유스케이스 조율 책임
- 외부 시스템 DTO가 핵심 로직에 직접 침투했는지 여부
## Architecture Review Guard

Clean Architecture 위반은 명확한 근거가 있을 때만 지적한다.

다음은 단독으로 Architecture 위반이라고 단정하지 않는다.

- 단순 문자열 검색
- 단순 if 분기
- 설정값 선택 로직
- prompt context 선택 로직
- 작은 private method
- 단순 DTO 매핑
- 단순 파일 경로 비교
- 단순 keyword matching

이런 항목은 필요하면 **MINOR** 또는 **SUGGESTION** 수준의 유지보수성 개선으로 다룬다.

## Test Review

Test Review는 다음 경우 수행한다.

- 비즈니스 로직 변경
- 조건문 또는 분기 추가
- 예외 처리 변경
- 정책 판단 로직 변경
- 트랜잭션 경계 변경
- 외부 API 호출 로직 변경
- 데이터 저장/수정/삭제 로직 변경
- 테스트 코드가 함께 변경되지 않음

테스트가 없다는 이유만으로 무조건 문제로 판단하지 않는다.

다음 정보를 함께 고려한다.

- 변경 위험도
- 변경된 코드의 책임
- **.ai-review/testing-policy.md**
- 기존 테스트 파일 존재 여부
- 변경 로직의 복잡도
## Security Review

Security Review는 다음 경우 수행한다.

- 인증/인가 코드 변경
- token, password, secret 처리 변경
- 외부 입력값 처리 변경
- SQL 또는 동적 쿼리 변경
- 파일 업로드/다운로드 변경
- 로그 출력 변경
- 개인정보 또는 민감정보 처리 변경
- 보안 설정 파일 변경

보안 이슈는 과소평가하지 않는다.
단, 근거 없이 취약점이 있다고 단정하지 않는다.

## Performance Review

Performance Review는 다음 경우 수행한다.

- 반복문 내부 DB/API 호출
- 대량 데이터 조회
- pagination 변경
- cache 로직 변경
- stream/collection 처리 변경
- 비동기 처리 변경
- lock 또는 동시성 관련 코드 변경
- N+1 가능성이 있는 조회 로직 변경

성능 문제는 입력 크기, 호출 빈도, 데이터 규모에 대한 근거가 있을 때 강하게 지적한다.

근거가 부족하면 **확인 필요**로 표시한다.

## Out of Scope

다음은 기본 리뷰 범위에서 제외한다.

P- R 변경사항과 무관한 기존 코드 전체 리팩토링 제안
- 제공되지 않은 파일에 대한 추측
- 빌드 결과 단정
- 테스트 실행 결과 단정
- 전체 시스템 성능 단정
- 배포 환경 문제 단정
- 작성자 의도에 대한 추측
- 코드 스타일 취향 논쟁
- .ai-review/review-ignore.md에 명시된 제외 대상

단, 보안상 명백히 위험한 변경은 ignore 대상이어도 리뷰할 수 있다.

## Severity Rule by Scope

리뷰 범위별 기본 severity 기준은 다음과 같다.

- 실제 장애, 보안, 데이터 손상 가능성 → BLOCKER
- 중요 로직 오류, 예외/트랜잭션 문제, 명확한 계층 위반 → MAJOR
- 유지보수성, 가독성, 테스트 보완 필요 → MINOR
- 선택적 리팩토링, 네이밍, 구조 개선 아이디어 → SUGGESTION

단순 구현 방식 개선을 BLOCKER 또는 MAJOR로 과장하지 않는다.

## No Evidence Rule

근거가 부족하면 다음 표현을 사용한다.
```text
확인 필요
제공된 컨텍스트만으로는 단정하기 어렵습니다.
관련 코드가 추가로 필요합니다.
```


근거가 부족한 내용을 단정하거나 높은 severity로 분류하지 않는다.

## Final Rule

AI 코드 리뷰어는 현재 Pull Request의 변경사항에 직접 도움이 되는 리뷰를 작성해야 한다.

넓게 많이 지적하는 것보다, 제공된 컨텍스트 안에서 정확하고 수정 가능한 문제를 찾는 것을 우선한다.