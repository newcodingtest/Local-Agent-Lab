
# GitHub Integration Policy

## Purpose

GitHub Webhook, GitHub API, 권한 범위, 댓글 게시 기준을 정의한다.

## Webhook Endpoint

기본 Webhook endpoint는 다음과 같다.

```text
POST /webhooks/github
```


## Required Headers

Webhook 요청에서 다음 header를 사용한다.

- X-GitHub-Event
- X-Hub-Signature-256

## Supported Events
처리 대상 event:
- ping
- pull_request

ping 이벤트는 pong을 반환한다.

pull_request 이벤트 중 다음 action만 리뷰한다.

- opened
- synchronize
- reopened
## Required GitHub API

서버는 다음 GitHub API를 사용한다.
```
GET /repos/{owner}/{repo}/pulls/{pull_number}/files
GET /repos/{owner}/{repo}/contents/{path}?ref={sha}
POST /repos/{owner}/{repo}/issues/{issue_number}/comments
```

PR 댓글은 issue comment API를 사용한다.

## Required Permissions

GitHub App 기준 권장 권한:

- Metadata: read
- Contents: read
- Pull requests: read
- Issues: write

PAT 기준 권장 권한:

- repository contents read
- pull request read
- issue comment write 

## File Content Rule

파일 원문은 PR의 head sha 기준으로 조회한다.

base branch 기준으로 변경 파일 원문을 조회하지 않는다.

## Security Rule
- Webhook secret 검증 실패 시 요청을 처리하지 않는다.
- GitHub token은 로그에 남기지 않는다.
- GitHub API 오류 응답의 민감정보를 PR 댓글에 게시하지 않는다.