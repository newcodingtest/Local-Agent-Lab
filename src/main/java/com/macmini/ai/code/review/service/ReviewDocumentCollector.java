package com.macmini.ai.code.review.service;

import com.macmini.ai.code.review.model.ReviewDocumentContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewDocumentCollector {

    private final GithubClient githubClient;

    private static final List<DocumentTarget> AI_REVIEW_DOCUMENTS = List.of(
            new DocumentTarget(".ai-review/project-profile.md", "PROJECT_PROFILE"),
            new DocumentTarget(".ai-review/architecture-rules.md", "ARCHITECTURE_RULES"),
            new DocumentTarget(".ai-review/package-role-map.md", "PACKAGE_ROLE_MAP"),
            new DocumentTarget(".ai-review/class-role-rules.md", "CLASS_ROLE_RULES"),
            new DocumentTarget(".ai-review/dependency-rules.md", "DEPENDENCY_RULES"),
            new DocumentTarget(".ai-review/coding-conventions.md", "CODING_CONVENTIONS"),
            new DocumentTarget(".ai-review/testing-policy.md", "TESTING_POLICY"),
            new DocumentTarget(".ai-review/error-handling-policy.md", "ERROR_HANDLING_POLICY"),
            new DocumentTarget(".ai-review/transaction-policy.md", "TRANSACTION_POLICY"),
            new DocumentTarget(".ai-review/security-policy.md", "SECURITY_POLICY"),
            new DocumentTarget(".ai-review/performance-policy.md", "PERFORMANCE_POLICY"),
            new DocumentTarget(".ai-review/review-focus.md", "REVIEW_FOCUS"),
            new DocumentTarget(".ai-review/review-ignore.md", "REVIEW_IGNORE")
    );

    private static final List<DocumentTarget> COMMON_DOCUMENTS = List.of(
            new DocumentTarget("README.md", "README"),
            new DocumentTarget("build.gradle", "BUILD_FILE"),
            new DocumentTarget("build.gradle.kts", "BUILD_FILE"),
            new DocumentTarget("pom.xml", "BUILD_FILE")
    );

    public List<ReviewDocumentContext> collect(
            final String owner,
            final String repo,
            final String ref
    ) {
        List<ReviewDocumentContext> result = new ArrayList<>();

        collectTargets(owner, repo, ref, AI_REVIEW_DOCUMENTS, result);
        collectTargets(owner, repo, ref, COMMON_DOCUMENTS, result);

        return result;
    }

    private void collectTargets(
            final String owner,
            final String repo,
            final String ref,
            final List<DocumentTarget> targets,
            final List<ReviewDocumentContext> result
    ) {
        for (DocumentTarget target : targets) {
            githubClient.getFileContent(owner, repo, target.path(), ref)
                    .map(this::limit)
                    .ifPresent(content -> result.add(new ReviewDocumentContext(
                            target.path(),
                            target.type(),
                            content
                    )));
        }
    }

    private String limit(final String content) {
        int maxLength = 8_000;

        if (content == null) {
            return "";
        }

        if (content.length() <= maxLength) {
            return content;
        }

        return content.substring(0, maxLength)
                + "\n\n<!-- document truncated for review context -->";
    }

    private record DocumentTarget(
            String path,
            String type
    ) {
    }
}