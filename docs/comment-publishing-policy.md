# Comment Publishing Policy

## Purpose

AI 리뷰 결과를 GitHub PR 댓글로 게시하는 기준을 정의한다.

## Publishing Rule

- 리뷰 결과는 PR conversation comment로 게시한다.
- PR 댓글 작성에는 issue comment API를 사용한다.
- LLM 응답이 비어 있으면 댓글을 게시하지 않는다.
- 검증 실패한 응답은 게시하지 않는다.
- 내부 오류 stacktrace를 PR 댓글에 게시하지 않는다.

## Duplicate Prevention

동일 PR의 동일 commit에 대해 중복 댓글을 반복 게시하지 않는다.

중복 방지 기준으로 다음 값을 사용할 수 있다.

- owner
- repository
- pull request number
- head commit sha
- review fingerprint

## Comment Content

댓글에는 다음 정보를 포함할 수 있다.

- AI Code Review 제목
- Summary
- Findings
- Check Needed
- Good Points
- 리뷰 기준이 된 commit sha

## Failure Rule

- GitHub API 401/403은 권한 문제로 기록하고 중단한다.
- GitHub API 404는 repository 또는 PR 정보를 확인한다.
- rate limit 발생 시 재시도 또는 큐잉한다.
- 댓글 게시 실패 시 LLM을 다시 호출하지 않는다.

## Do Not

- 민감정보를 댓글에 게시하지 않는다.
- GitHub token을 댓글에 게시하지 않는다.
- 내부 stacktrace를 댓글에 게시하지 않는다.
- 실패한 리뷰를 성공처럼 게시하지 않는다.