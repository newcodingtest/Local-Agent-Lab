# Test Strategy

## Purpose

AI 코드 리뷰어 서버의 핵심 기능 테스트 기준을 정의한다.

## Unit Test Targets

우선 테스트 대상은 다음과 같다.

- GithubSignatureVerifier
- GithubWebhookApi event filtering
- PullRequestReviewService
- GithubClient
- ReviewContextBuilder
- ReviewDocumentCollector
- ContextSelectionService
- ReviewPromptFactory
- AiReviewService
- Comment publishing

## Unit Test Rules

단위 테스트는 다음을 검증한다.

- `opened`, `synchronize`, `reopened` action만 처리하는지
- 지원하지 않는 event/action은 무시하는지
- signature 검증 실패 시 처리하지 않는지
- 변경 파일 원문을 headRef 기준으로 수집하는지
- `.ai-review` 문서를 올바른 type으로 수집하는지
- 제외 파일을 수집하지 않는지
- prompt에 필수 규칙이 포함되는지
- severity 규칙이 prompt에 포함되는지
- LLM 응답이 비어 있으면 예외 처리하는지

## Integration Test Rules

통합 테스트는 mock 기반으로 다음 흐름을 검증한다.

1. sample webhook payload 입력
2. PR files API mock
3. file contents API mock
4. ReviewContext 생성
5. Prompt 생성
6. LLM 응답 mock
7. PR comment API mock

## Do Not

- 기본 테스트가 실제 GitHub API에 의존하지 않도록 한다.
- 기본 테스트가 실제 Ollama 모델 응답에 의존하지 않도록 한다.
- secret이 필요한 테스트를 기본 실행에 포함하지 않는다.
- 외부 네트워크 상태에 따라 실패하는 테스트를 기본 테스트로 만들지 않는다.

## MVP Test Priority

MVP에서는 다음 테스트를 우선 작성한다.

1. Webhook 이벤트 필터링
2. GitHub PR files 응답 파싱
3. `.ai-review` 문서 수집
4. ContextSelectionService 선택 규칙
5. ReviewPromptFactory prompt 생성
6. PR 댓글 게시 mock 테스트