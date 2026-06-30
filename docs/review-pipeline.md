# Review Pipeline

## Pipeline

PR 리뷰 파이프라인은 다음 순서로 동작한다.

1. GitHub Webhook 수신
2. GitHub signature 검증
3. Pull Request 이벤트 필터링
4. PR 메타데이터 추출
5. PR 변경 파일 목록 수집
6. 변경 파일 diff 생성
7. 변경 파일 원문 수집
8. `.ai-review` 문서 수집
9. README / build 파일 수집
10. 관련 파일 / 테스트 파일 수집
11. LLM prompt에 넣을 컨텍스트 선택
12. LLM prompt 생성
13. qwen3-coder:30b 모델 호출
14. 리뷰 Markdown 생성
15. GitHub PR 댓글 게시
16. 처리 결과 로깅

## Event Rules

처리 대상 GitHub event는 다음으로 제한한다.

- `pull_request`

처리 대상 action은 다음으로 제한한다.

- `opened`
- `synchronize`
- `reopened`

그 외 event/action은 로그만 남기고 리뷰하지 않는다.

## Required Metadata

PR 처리에는 다음 정보가 필요하다.

- owner
- repository name
- repository full name
- pull request number
- base branch
- head commit sha
- PR title
- PR body
- changed files

## Service Flow

권장 흐름은 다음과 같다.

```text
GithubWebHookApi
 → PullRequestReviewService
 → GithubClient
 → ReviewContextBuilder
 → ReviewDocumentCollector
 → ContextSelectionService
 → ReviewPromptFactory
 → AiReviewService
 → GithubClient.createIssueComment