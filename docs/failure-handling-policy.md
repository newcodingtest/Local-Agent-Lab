# Failure Handling Policy

## Purpose

GitHub API, LLM, 문서 수집, timeout 등 실패 상황 처리 기준을 정의한다.

## Stop Conditions

다음 경우 리뷰를 중단한다.

- GitHub signature 검증 실패
- PR metadata 추출 실패
- changed files 수집 실패
- GitHub 인증 실패
- LLM 호출 실패
- LLM 응답이 비어 있음
- 리뷰 결과 검증 실패

## Continue Conditions

다음 경우 리뷰를 계속할 수 있다.

- README 없음
- `.ai-review` 문서 없음
- 일부 `.ai-review` 문서 수집 실패
- 일부 관련 파일 수집 실패
- 테스트 파일 없음
- build 파일 없음

## Fallback

- `.ai-review` 문서 없음 → 일반 Java/Spring 기준으로 제한 리뷰
- README 없음 → README 없이 리뷰
- 변경 파일 원문 일부 실패 → diff 기준으로 제한 리뷰
- 관련 파일 수집 실패 → 변경 파일 중심으로 리뷰
- 댓글 게시 실패 → 로그 기록 후 재시도 가능 상태로 남김

## Logging Rule

실패 로그에는 다음 정보를 포함한다.

- requestId
- owner
- repository
- pull request number
- failure step
- failure reason

민감정보는 로그에 포함하지 않는다.

## Do Not

- 실패한 LLM 응답을 그대로 게시하지 않는다.
- 실패 원인에 token이나 secret을 포함하지 않는다.
- 실패를 성공처럼 기록하지 않는다.
- 일부 optional context 실패를 전체 장애로 과장하지 않는다.