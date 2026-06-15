package com.macmini.ai.code.review.service;

import com.macmini.ai.code.review.model.*;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ReviewPromptFactory {

    public String createUserPrompt(final ReviewContext context) {
        return """
                Repository: %s
                Pull Request: #%d
                Base Branch: %s

                너는 Java/Spring 백엔드 프로젝트를 리뷰하는 시니어 코드리뷰어다.
                이번 리뷰의 목적은 Clean Code와 Clean Architecture 관점의 구조적 피드백이다.

                ## Review Scope

                - 리뷰 대상은 반드시 PR diff에서 변경된 코드다.
                - 단, 판단 근거로 변경 파일 전체 내용, 관련 파일, 테스트 파일, 리뷰 규칙을 사용할 수 있다.
                - 변경되지 않은 기존 코드 자체만의 문제는 주요 리뷰로 지적하지 말고, 필요한 경우 "참고"로만 작성한다.
                - diff와 관련 없는 일반론은 작성하지 않는다.
                - 사소한 포맷팅, 취향 수준의 스타일 지적은 제외한다.

                ## Priority

                1. 책임 분리
                2. 계층 간 의존성 방향
                3. 추상화 수준 일관성
                4. 메서드/클래스 크기와 역할
                5. 중복 제거
                6. 네이밍
                7. 테스트 가능성
                8. 예외/트랜잭션 경계

                ## Output Format

                문제가 없으면 억지로 지적하지 말고 "주요 구조적 문제는 발견되지 않았습니다."라고 작성한다.

                문제가 있다면 아래 형식으로 작성한다.

                ### [Severity] 제목

                - 위치:
                - 문제:
                - 근거:
                - 개선 방향:
                - 예시 코드:

                Severity는 CRITICAL, HIGH, MEDIUM, LOW 중 하나를 사용한다.

                ---

                # Clean Code / Clean Architecture Review Rules

                %s

                ---

                # PR Diff

                %s

                ---

                # Changed File Full Contents

                %s

                ---

                # Related Files

                %s

                ---

                # Related Tests

                %s
                """.formatted(
                context.repository(),
                context.pullNumber(),
                context.baseBranch(),
                formatKnowledge(context),
                context.diffText(),
                formatChangedFiles(context),
                formatRelatedFiles(context),
                formatTestFiles(context)
        );
    }

    private String formatKnowledge(final ReviewContext context) {
        return context.knowledgeList().stream()
                .map(this::formatKnowledge)
                .collect(Collectors.joining("\n\n---\n\n"));
    }

    private String formatKnowledge(final RetrievedReviewKnowledge knowledge) {
        return """
                Source: %s
                Title: %s

                %s
                """.formatted(
                knowledge.source(),
                knowledge.title(),
                knowledge.content()
        );
    }

    private String formatChangedFiles(final ReviewContext context) {
        return context.changedFiles().stream()
                .map(file -> """
                        ## %s
                        Status: %s

                        ```java
                        %s
                        ```
                        """.formatted(
                        file.path(),
                        file.status(),
                        file.content()
                ))
                .collect(Collectors.joining("\n\n"));
    }

    private String formatRelatedFiles(final ReviewContext context) {
        if (context.relatedFiles().isEmpty()) {
            return "No related files found.";
        }

        return context.relatedFiles().stream()
                .map(file -> """
                        ## %s
                        Reason: %s

                        ```java
                        %s
                        ```
                        """.formatted(
                        file.path(),
                        file.reason(),
                        file.content()
                ))
                .collect(Collectors.joining("\n\n"));
    }

    private String formatTestFiles(final ReviewContext context) {
        if (context.testFiles().isEmpty()) {
            return "No related tests found.";
        }

        return context.testFiles().stream()
                .map(file -> """
                        ## %s

                        ```java
                        %s
                        ```
                        """.formatted(
                        file.path(),
                        file.content()
                ))
                .collect(Collectors.joining("\n\n"));
    }
}
