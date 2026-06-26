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

            case CODE_REVIEW:
                return LlmModelProfile.builder()
                        .model("qwen3-coder:30b")
                        .temperature(0.1)
                        .numPredict(4096)
                        .systemPrompt("""
                    너는 GitHub Pull Request를 리뷰하는 시니어 백엔드 코드 리뷰어다.

                    리뷰 기준:
                    1. 버그 가능성
                    2. 예외 처리 누락
                    3. null 처리 문제
                    4. 동시성/트랜잭션 문제
                    5. 성능 문제
                    6. 보안 문제
                    7. 테스트 누락
                    8. Clean Code
                    9. SOLID
                    10. Clean Architecture
                    11. 프로젝트 .ai-review 문서의 규칙 위반

                    반드시 지킬 규칙:
                    - 제공된 PR diff, 변경 파일 원문, .ai-review 문서, README, 관련 파일만 근거로 리뷰하라.
                    - 존재하지 않는 파일, 클래스, 메서드, 정책을 만들어내지 마라.
                    - 빌드나 테스트를 실행한 것처럼 말하지 마라.
                    - 확실하지 않은 내용은 `확인 필요`로 표시하라.
                    - 단순 취향성 리뷰보다 안정성, 유지보수성, 테스트 가능성, 아키텍처 일관성을 우선하라.
                    - 문제를 지적할 때는 심각도, 근거, 수정 방향을 함께 제시하라.
                    - PR 변경사항과 직접 관련 없는 일반론은 작성하지 마라.
                    """)
                        .build();

            case ARCHITECTURE_REVIEW:
                return LlmModelProfile.builder()
                        .model("qwen3:30b")
                        .temperature(0.2)
                        .numPredict(4096)
                        .systemPrompt("""
                    너는 시니어 백엔드 아키텍처 리뷰어다.
                    PR diff를 기반으로 구조적 문제와 설계 개선점을 검토하라.

                    리뷰 기준:
                    1. 책임 분리
                    2. SOLID
                    3. 도메인 경계
                    4. 의존성 방향
                    5. 확장성
                    6. 운영 안정성

                    diff에서 추론 가능한 범위 안에서만 리뷰하라.
                    """)
                        .build();

            default:
                throw new IllegalArgumentException("Unsupported taskType: " + taskType);
        }
    }
}
