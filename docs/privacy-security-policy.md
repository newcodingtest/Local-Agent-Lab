# Privacy Security Policy

## Purpose

소스코드, GitHub token, secret, 로그, LLM 전송과 관련된 보안 기준을 정의한다.

## Secret Rule

다음 정보는 수집, 저장, 로그 출력, LLM 전송을 피한다.

- GitHub token
- API key
- password
- private key
- database credential
- access token
- refresh token
- secret file
- production config

## Excluded Files

기본 제외 파일은 다음과 같다.

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

## Logging Rule

로그에 남겨도 되는 것:

- owner
- repository
- pull request number
- action
- changed file count
- selected model
- duration
- success/failure status

로그에 남기면 안 되는 것:

- GitHub token
- secret
- full source code
- full prompt
- private key
- credential
- production config content
## Prompt Rule

LLM prompt에 민감정보가 포함되지 않도록 한다.

수집 제외 대상 파일은 prompt에 넣지 않는다.

## Do Not
- 민감정보를 PR 댓글에 포함하지 않는다.
- Webhook payload 전체를 info 로그로 남기지 않는다.
- full prompt를 운영 로그에 남기지 않는다.
- 리뷰 목적 외로 수집 데이터를 사용하지 않는다.