# Token Budget Policy

## Purpose

LLM 컨텍스트 길이 제한에 맞춰 prompt에 포함할 정보의 우선순위를 정의한다.

## Priority

컨텍스트 우선순위는 다음과 같다.

1. PR diff
2. 변경 파일 patch
3. 변경 파일 전체 원문
4. `.ai-review` 핵심 문서
5. 관련 테스트 파일
6. 관련 소스 파일
7. README
8. build 파일
9. 디렉터리 구조 요약

## Compression Rule

컨텍스트가 너무 크면 다음 순서로 줄인다.

1. README를 요약하거나 제외한다.
2. build 파일을 요약한다.
3. 디렉터리 구조를 제외한다.
4. 관련 파일 수를 줄인다.
5. 테스트 파일 수를 줄인다.
6. `.ai-review` 문서 중 관련도가 낮은 문서를 제외한다.
7. 변경 파일 원문을 길이 제한한다.
8. diff는 마지막까지 유지한다.

## Recommended Limits

MVP 권장 제한:

- diff: 최대 50,000 characters
- changed file content: 파일당 최대 12,000 characters
- review document: 파일당 최대 8,000 characters
- selected review documents: 최대 10개
- related files: 최대 5개
- test files: 최대 5개

## Truncation Rule

컨텍스트를 자르면 잘린 사실을 명시한다.

예시:

```text
... content truncated for review context ...
```

## Do Not
- 컨텍스트 초과 상태로 LLM을 호출하지 않는다.
- 모든 파일 원문을 무조건 넣지 않는다.
- 중복 컨텍스트를 반복해서 넣지 않는다.
- 민감정보를 줄이지 않고 그대로 넣지 않는다.