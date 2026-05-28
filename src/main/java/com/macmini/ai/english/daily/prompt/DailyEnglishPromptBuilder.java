package com.macmini.ai.english.daily.prompt;

import com.macmini.ai.english.daily.api.request.DailyEnglishContentRequest;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DailyEnglishPromptBuilder {


	private static final Map<Integer, String> CURRICULUM = Map.ofEntries(
			Map.entry(1, "현재완료 (Present Perfect)"),
			Map.entry(2, "현재완료진행형 (Present Perfect Continuous)"),
			Map.entry(3, "과거진행형 (Past Continuous)"),
			Map.entry(4, "과거완료 (Past Perfect)"),
			Map.entry(5, "조건문 2형"),
			Map.entry(6, "조건문 3형"),
			Map.entry(7, "수동태 (Passive Voice)"),
			Map.entry(8, "간접의문문 (Indirect Questions)"),
			Map.entry(9, "사역동사&지각동사"),
			Map.entry(10, "관계부사"),
			Map.entry(11, "동명사 (Gerund)"),
			Map.entry(12, "to부정사"),
			Map.entry(13, "동명사 vs to부정사"),
			Map.entry(14, "강조구문 (It is ~ that)"),
			Map.entry(15, "추측 조동사"),
			Map.entry(16, "간접화법"),
			Map.entry(17, "시제 일치"),
			Map.entry(18, "가정법 과거"),
			Map.entry(19, "가정법 과거완료"),
			Map.entry(20, "should / must / have to"),
			Map.entry(21, "분사구문"),
			Map.entry(22, "도치구문"),
			Map.entry(23, "get 수동태"),
			Map.entry(24, "관사 심화"),
			Map.entry(25, "구동사 (Phrasal Verbs)"),
			Map.entry(26, "상관 접속사 & 연결어"),
			Map.entry(27, "관계절"),
			Map.entry(28, "명사절 접속사"),
			Map.entry(29, "비교급 & 최상급 심화"),
			Map.entry(30, "가정법 혼합 및 I wish / As if")
	);

	public String buildDraftPrompt(final DailyEnglishContentRequest request) {
		String topic = nullToDefault(
				request.getTopic(),
				CURRICULUM.getOrDefault(request.getDay(), "영어 문법")
		);

		return """
                당신은 한국인 학습자를 위한 영어 문법 교육 전문가입니다.

                오늘 반드시 다루어야 할 주제는 오직 "%s" 입니다.
                다른 문법 주제로 벗어나지 마세요.

                콘텐츠 타입: %s
                날짜: %d-%02d-%02d
                대상 학습자: 한국인 20~30대 초중급 영어 학습자

                반드시 JSON 형식으로만 응답하세요.
                마크다운 코드블록은 절대 사용하지 마세요.
                JSON 외 설명 문장은 붙이지 마세요.

                JSON 구조:
                {
                  "title": "",
                  "summary": "",
                  "level": "",
                  "mainExpression": "",
                  "explanationKo": "",
                  "situation": "",
                  "structure": "",
                  "examples": [
                    {
                      "english": "",
                      "korean": "",
                      "point": ""
                    },
                    {
                      "english": "",
                      "korean": "",
                      "point": ""
                    },
                    {
                      "english": "",
                      "korean": "",
                      "point": ""
                    },
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

                콘텐츠 품질 기준:
                1. "I am fine, thank you" 같은 교과서식 예문은 금지합니다.
                2. 실제 원어민이 쓸 법한 자연스러운 문장을 사용하세요.
                3. 각 예문은 상황이 바로 떠오르게 구체적으로 작성하세요.
                4. 한국어 번역은 너무 딱딱하지 않게, 20~30대가 이해하기 쉬운 자연스러운 말투로 작성하세요.
                5. 단, 과한 유행어 또는 어색한 밈 표현은 피하세요.
                6. explanationKo는 문법 개념, 사용 상황, 한국인이 자주 헷갈리는 포인트를 포함하세요.
                7. point는 해당 예문에서 학습자가 봐야 할 문법 포인트를 짧게 설명하세요.
                8. quiz는 오늘 주제를 실제로 이해했는지 확인할 수 있는 문제로 만드세요.
                """.formatted(
				topic,
				request.getContentType(),
				request.getYear(),
				request.getMonth(),
				request.getDay()
		);
	}

	public String buildReviewPrompt(final String draft) {
		return """
                아래 영어 학습 콘텐츠 초안을 검수해줘.

                검수 기준:
                1. 영어 문장이 실제 원어민 표현처럼 자연스러운지.
                2. 오늘의 주제에서 벗어나지 않았는지.
                3. 한국어 설명이 한국인 학습자에게 정확하고 쉬운지.
                4. 예문마다 구체적인 상황이 느껴지는지.
                5. 교과서식 표현, 어색한 번역투, 과한 유행어가 없는지.
                6. JSON 구조가 깨지지 않았는지.

                검수 결과도 JSON으로 반환해.
                마크다운 코드블록은 사용하지 마.

                응답 구조:
                {
                  "valid": true,
                  "issues": [],
                  "improvementDirection": "",
                  "recommendedFixes": []
                }

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
                6. trailing comma는 제거해.
                7. examples는 반드시 4개를 유지해.

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
                3. 기존 JSON 구조는 유지해.
                4. 영어 예문은 자연스럽고 실제 사용 가능한 표현으로 다듬어.
                5. 한국어 설명은 학습자 친화적으로 다듬어.
                6. 과한 유행어, 어색한 번역투, 교과서식 표현은 제거해.
                7. examples는 반드시 4개를 유지해.

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
