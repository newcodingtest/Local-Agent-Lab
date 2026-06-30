# Local Development Guide

## Purpose

로컬 개발, 실행, 테스트, Ollama 설정, GitHub Webhook 테스트 방법을 정의한다.

## Required Environment

필요 환경은 다음과 같다.

- Java 17
- Gradle
- Spring Boot
- Ollama
- GitHub token 또는 GitHub App credentials
- GitHub Webhook secret

## Recommended Local Model

MVP 기본 코드 리뷰 모델은 다음을 사용한다.

```text
qwen3-coder:30b
```

## Environment Variables

예시 환경변수:
```text
GITHUB_TOKEN=...
GITHUB_WEBHOOK_SECRET=...
OLLAMA_BASE_URL=http://localhost:11434
AI_REVIEW_MODEL=qwen3-coder:30b
```

민감정보는 코드에 하드코딩하지 않는다.

## Local Run

서버 실행 전 확인한다.

- Spring Boot 서버 포트
- GitHub Webhook endpoint
- Ollama 실행 여부
- GitHub token 권한
- Webhook secret 설정
- PR 댓글 작성 권한
## Webhook Test

로컬 Webhook 테스트 방법:

- ngrok 또는 Cloudflare Tunnel 사용
- GitHub Webhook URL에 터널 주소 등록
- pull_request 이벤트 활성화
- secret 설정
- GitHub Webhook redelivery로 테스트
## Basic Test Flow
- 서버 실행
- GitHub Webhook ping 확인
- 테스트 PR 생성
- Webhook 수신 로그 확인
- PR files 수집 로그 확인
- .ai-review 문서 수집 로그 확인
- qwen3-coder 호출 확인
- PR 댓글 생성 확인
## Do Not
- 운영 token을 로컬 로그에 출력하지 않는다.
- 테스트용 PR에 반복 댓글을 대량 생성하지 않는다.
- 로컬 설정 파일을 운영 설정으로 commit하지 않는다.
- .env 파일을 Git에 올리지 않는다.