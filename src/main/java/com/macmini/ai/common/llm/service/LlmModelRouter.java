package com.macmini.ai.common.llm.service;

import com.macmini.ai.common.llm.model.LlmModelProfile;
import com.macmini.ai.common.llm.model.LlmTaskType;
import org.springframework.stereotype.Component;

@Component
public class LlmModelRouter {

    public LlmModelProfile route(final LlmTaskType taskType) {
        if (taskType == null) {
            throw new IllegalArgumentException("taskType must not be null");
        }

        switch (taskType) {
            case CONTENT_GENERATION:
                return LlmModelProfile.builder()
                        .model("qwen3:30b")
                        .temperature(0.7)
                        .numPredict(4096)
                        .systemPrompt("""
								너는 영어 학습 콘텐츠 생성 에이전트다.
								반드시 사용자가 요구한 출력 형식을 지켜라.
								일일 학습 콘텐츠로 바로 사용할 수 있는 품질로 작성하라.
								""")
                        .build();

            case ENGLISH_REVIEW:
                return LlmModelProfile.builder()
                        .model("gemma3:27b")
                        .temperature(0.2)
                        .numPredict(2048)
                        .systemPrompt("""
								너는 영어 문장 검수 에이전트다.
								문법, 자연스러움, 뉘앙스, 학습자 관점의 설명을 점검하라.
								원문의 의도는 보존하고 불필요한 창작은 하지 마라.
								""")
                        .build();

            case FINAL_REWRITE:
                return LlmModelProfile.builder()
                        .model("qwen3:30b")
                        .temperature(0.4)
                        .numPredict(4096)
                        .systemPrompt("""
								너는 최종 콘텐츠 편집 에이전트다.
								초안과 검수 결과를 바탕으로 최종 배포 가능한 결과물을 작성하라.
								출력 형식은 반드시 사용자의 요구사항을 따른다.
								""")
                        .build();

            case JSON_VALIDATE:
                return LlmModelProfile.builder()
                        .model("qwen3.5:9b")
                        .temperature(0.0)
                        .numPredict(2048)
                        .systemPrompt("""
								너는 JSON 검증 에이전트다.
								JSON 문법 오류, 누락 필드, 타입 오류를 점검하라.
								가능하면 설명 없이 수정된 JSON만 반환하라.
								""")
                        .build();

            default:
                throw new IllegalArgumentException("Unsupported taskType: " + taskType);
        }
    }
}
