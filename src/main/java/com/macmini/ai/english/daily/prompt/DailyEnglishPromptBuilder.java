package com.macmini.ai.english.daily.prompt;

import com.macmini.ai.english.daily.api.request.DailyEnglishContentRequest;
import org.springframework.stereotype.Component;

@Component
public class DailyEnglishPromptBuilder {

    public String buildDraftPrompt(final DailyEnglishContentRequest request) {
        return """
				다음 조건으로 일일 영어 학습 콘텐츠를 생성해줘.

				콘텐츠 타입: %s
				날짜: %d-%02d-%02d
				주제: %s

				반드시 JSON 형식으로만 응답해.
				마크다운 코드블록은 사용하지 마.

				JSON 구조:
				{
				  "title": "",
				  "summary": "",
				  "level": "",
				  "mainExpression": "",
				  "explanationKo": "",
				  "examples": [
				    {
				      "english": "",
				      "korean": "",
				      "point": ""
				    }
				  ],
				  "quiz": {
				    "question": "",
				    "answer": "",
				    "explanation": ""
				  }
				}
				""".formatted(
                request.getContentType(),
                request.getYear(),
                request.getMonth(),
                request.getDay(),
                nullToDefault(request.getTopic(), "자동 선정")
        );
    }

    public String buildReviewPrompt(final String draft) {
        return """
				아래 영어 학습 콘텐츠 초안을 검수해줘.

				검수 기준:
				1. 영어 문장이 자연스러운지.
				2. 한국어 설명이 정확한지.
				3. 학습자가 이해하기 쉬운지.
				4. JSON 구조가 깨지지 않았는지.
				5. 개선이 필요한 부분이 있으면 수정 방향을 제시해줘.

				초안:
				%s
				""".formatted(draft);
    }

    public String buildJsonValidationPrompt(final String content) {
        return """
				아래 내용을 유효한 JSON으로 정리해줘.

				조건:
				1. JSON만 반환해.
				2. 마크다운 코드블록을 사용하지 마.
				3. 설명 문장을 붙이지 마.
				4. 누락된 필드는 합리적으로 보완해.
				5. 문자열은 반드시 double quote를 사용해.

				내용:
				%s
				""".formatted(content);
    }

    public String buildFinalRewritePrompt(final String validatedJson, final String review) {
        return """
				아래 JSON 콘텐츠와 검수 결과를 바탕으로 최종 배포용 JSON을 작성해줘.

				조건:
				1. JSON만 반환해.
				2. 마크다운 코드블록을 사용하지 마.
				3. 영어 예문은 자연스럽게 유지해.
				4. 한국어 설명은 학습자 친화적으로 다듬어.
				5. 기존 JSON 구조는 유지해.

				검증된 JSON:
				%s

				검수 결과:
				%s
				""".formatted(validatedJson, review);
    }

    private String nullToDefault(final String value, final String defaultValue) {
        if (value == null || value.isBlank()) {
            return defaultValue;
        }

        return value;
    }
}
