package com.macmini.ai.code.review.service;

import com.macmini.ai.code.review.model.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReviewPromptFactory {

    public String createUserPrompt(final ReviewContext context) {
        return """
                당신은 GitHub Pull Request를 리뷰하는 AI 코드 리뷰어입니다.

                ## Review Rules

                - 제공된 컨텍스트 안에서만 판단하세요.
                - 존재하지 않는 파일, 클래스, 메서드, 정책을 만들어내지 마세요.
                - 확실하지 않은 내용은 `확인 필요`로 표시하세요.
                - 단순 취향성 리뷰는 피하세요.
                - PR 변경사항과 직접 관련 있는 문제만 리뷰하세요.
                - 문제를 지적할 때는 심각도, 근거, 수정 방향을 함께 제시하세요.
                - 테스트를 실행하지 않았으므로 테스트 실행 결과를 단정하지 마세요.
                - 빌드를 실행하지 않았으므로 컴파일 성공/실패를 단정하지 마세요.

                ## Pull Request

                - Repository: %s
                - Pull Number: #%d
                - Base Branch: %s
                - Head Commit: %s

                ## Project Review Documents

                %s

                ## Changed Files

                %s

                ## Pull Request Diff

                %s

                ## Changed File Contents

                %s

                ## Related Files

                %s

                ## Test Files

                %s

                ## Output Format

                다음 Markdown 형식으로 답변하세요.

                # AI Code Review

                ## Summary
                변경사항 요약을 3줄 이내로 작성하세요.

                ## Findings

                문제가 있으면 아래 형식을 반복하세요.

                ### [SEVERITY] 제목

                - File: `파일 경로`
                - Location: 클래스/메서드/라인 정보. 알 수 없으면 `확인 필요`
                - Reason: 문제 이유
                - Suggestion: 수정 방향

                허용 severity:
                - BLOCKER
                - MAJOR
                - MINOR
                - SUGGESTION

                ## Check Needed

                컨텍스트 부족으로 단정할 수 없는 항목이 있으면 작성하세요.

                ## Good Points

                의미 있는 개선점이 있으면 짧게 작성하세요.

                문제가 명확히 없으면 억지로 만들지 말고 다음 문장을 포함하세요.

                `제공된 컨텍스트 기준으로 명확한 문제는 발견하지 못했습니다.`
                """.formatted(
                context.repository(),
                context.pullNumber(),
                context.baseBranch(),
                context.headRef(),
                formatReviewDocuments(context.reviewDocuments()),
                formatChangedFileList(context.changedFiles()),
                context.diffText(),
                formatChangedFileContents(context.changedFiles()),
                formatRelatedFiles(context.relatedFiles()),
                formatTestFiles(context.testFiles())
        );
    }

    private String formatReviewDocuments(final java.util.List<ReviewDocumentContext> documents) {
        if (documents == null || documents.isEmpty()) {
            return "No project review documents were provided.";
        }

        return documents.stream()
                .map(document -> """
                        ### %s
                        Path: `%s`

                        ```md
                        %s
                        ```
                        """.formatted(
                        document.type(),
                        document.path(),
                        document.content()
                ))
                .collect(Collectors.joining("\n\n"));
    }

    private String formatChangedFileList(final List<ChangedFileContext> files) {
        if (files == null || files.isEmpty()) {
            return "No changed file content was provided.";
        }

        return files.stream()
                .map(file -> "- `%s` (%s)".formatted(file.path(), file.status()))
                .collect(Collectors.joining("\n"));
    }

    private String formatChangedFileContents(final java.util.List<ChangedFileContext> files) {
        if (files == null || files.isEmpty()) {
            return "No changed file contents were provided.";
        }

        return files.stream()
                .map(file -> """
                        ### File: %s

                        ```text
                        %s
                        ```
                        """.formatted(
                        file.path(),
                        file.content()
                ))
                .collect(Collectors.joining("\n\n"));
    }

    private String formatRelatedFiles(final java.util.List<RelatedFileContext> files) {
        if (files == null || files.isEmpty()) {
            return "No related files were provided.";
        }

        return files.stream()
                .map(file -> """
                        ### Related File: %s

                        ```text
                        %s
                        ```
                        """.formatted(
                        file.path(),
                        file.content()
                ))
                .collect(Collectors.joining("\n\n"));
    }

    private String formatTestFiles(final java.util.List<TestFileContext> files) {
        if (files == null || files.isEmpty()) {
            return "No test files were provided.";
        }

        return files.stream()
                .map(file -> """
                        ### Test File: %s

                        ```text
                        %s
                        ```
                        """.formatted(
                        file.path(),
                        file.content()
                ))
                .collect(Collectors.joining("\n\n"));
    }
}
