# Review Output Format

## Purpose

AI 코드 리뷰 결과의 Markdown 출력 형식을 정의한다.

## Required Format

리뷰 댓글은 다음 구조를 따른다.


# AI Code Review

## Summary

PR 변경사항 요약을 3줄 이내로 작성한다.

## Findings

### [SEVERITY] 제목

- File: `path/to/File.java`
- Location: 클래스/메서드/라인 정보. 알 수 없으면 `확인 필요`
- Reason: 문제 이유
- Suggestion: 수정 방향

## Check Needed

컨텍스트 부족으로 단정하기 어려운 항목을 작성한다.

## Good Points

의미 있는 개선점이 있으면 짧게 작성한다.

## Allowed Severity

허용되는 severity는 다음뿐이다.

- BLOCKER
- MAJOR
- MINOR
- SUGGESTION

다음 severity는 사용하지 않는다.

- HIGH
- MEDIUM
- LOW
- CRITICAL
- INFO
## Finding Rule

각 finding은 다음 조건을 만족해야 한다.

- 실제 변경사항과 관련 있어야 한다.
- 파일 경로는 제공된 컨텍스트 안에 있어야 한다.
- 문제 이유와 수정 방향을 함께 제시해야 한다.
- 근거가 부족하면 확인 필요로 표시해야 한다.
## No Issue Format

명확한 문제가 없으면 억지로 이슈를 만들지 않는다.

다음 문장을 포함한다.
```text
제공된 컨텍스트 기준으로 명확한 문제는 발견하지 못했습니다.
```

## Do Not
- 코드 전체를 길게 복사하지 않는다.
- PR과 직접 관련 없는 일반론을 작성하지 않는다.
- 없는 파일 경로를 만들지 않는다.
- 없는 line number를 만들지 않는다.