# Context Collection Policy

## Purpose

PR 리뷰에 필요한 코드와 문서를 GitHub API로 직접 수집하는 기준을 정의한다.

현재 MVP는 RAG를 사용하지 않는다.

## Required Context

항상 우선 수집한다.

- PR metadata
- PR changed files
- file patch / diff
- 변경 파일 전체 원문
- 클라이언트 프로젝트의 `.ai-review/` 핵심 문서
- README.md
- build.gradle 또는 pom.xml

## Optional Context

필요할 때만 수집한다.

- 변경 파일의 관련 테스트 파일
- 변경 클래스가 구현하는 interface
- 변경 클래스가 상속하는 parent class
- 같은 package의 주요 연관 클래스
- 프로젝트 디렉터리 구조 요약

## AI Review Documents

우선 수집 대상은 다음과 같다.

```text
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
```

## Common Documents

공통 수집 대상은 다음과 같다.
```
README.md
build.gradle
build.gradle.kts
pom.xml
settings.gradle
settings.gradle.kts
```

## Exclude

다음은 기본 수집하지 않는다.
```text
.git/
.gradle/
.idea/
build/
target/
out/
node_modules/
dist/
coverage/
*.class
*.jar
*.war
*.zip
*.png
*.jpg
*.jpeg
*.gif
*.pdf
*.lock
```

민감정보 파일은 수집하지 않는다.
```text
.env
.env.*
application-prod.yml
application-prod.properties
secrets.yml
secret.yml
private-key.pem
*.key
*.p12
*.jks
```

## Rule

수집되지 않은 파일을 근거로 리뷰하지 않는다.

컨텍스트가 부족하면 확인 필요로 표시한다.

문서 수집 실패는 전체 리뷰 실패로 보지 않는다.
단, PR diff와 changed files 수집 실패는 리뷰를 중단한다.