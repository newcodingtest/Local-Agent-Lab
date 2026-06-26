package com.macmini.ai.code.review.service;
import com.macmini.ai.code.review.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ReviewContextBuilder {

    private final GithubClient githubClient;
    private final RelatedFileResolver relatedFileResolver;
    private final ReviewDocumentCollector reviewDocumentCollector;
    private final ContextSelectionService contextSelectionService;

    public ReviewContext build(
            final String owner,
            final String repo,
            final String repository,
            final int pullNumber,
            final String baseBranch,
            final String headRef,
            final String diffText,
            final List<GithubPullRequestFile> pullRequestFiles
    ) {
        List<ChangedFileContext> changedFiles = pullRequestFiles.stream()
                .filter(this::isReviewableSourceFile)
                .filter(file -> !"removed".equals(file.status()))
                .map(file -> toChangedFileContext(owner, repo, headRef, file))
                .filter(Objects::nonNull)
                .toList();

        List<RelatedFileContext> relatedFiles = relatedFileResolver.resolve(
                owner,
                repo,
                headRef,
                changedFiles
        );

        List<TestFileContext> testFiles = relatedFileResolver.resolveTests(
                owner,
                repo,
                headRef,
                changedFiles
        );

        List<ReviewDocumentContext> collectedDocuments =
                reviewDocumentCollector.collect(owner, repo, headRef);

        List<ReviewDocumentContext> selectedDocuments =
                contextSelectionService.select(collectedDocuments, changedFiles, diffText);

        return new ReviewContext(
                owner,
                repo,
                repository,
                pullNumber,
                baseBranch,
                headRef,
                diffText,
                changedFiles,
                relatedFiles,
                testFiles,
                selectedDocuments
        );
    }

    private ChangedFileContext toChangedFileContext(
            final String owner,
            final String repo,
            final String ref,
            final GithubPullRequestFile file
    ) {
        return githubClient.getFileContent(owner, repo, file.filename(), ref)
                .map(content -> new ChangedFileContext(
                        file.filename(),
                        file.status(),
                        file.patch(),
                        limit(content)
                ))
                .orElse(null);
    }

    private boolean isReviewableSourceFile(final GithubPullRequestFile file) {
        String filename = file.filename();

        return filename.endsWith(".java")
                || filename.endsWith(".kt")
                || filename.endsWith(".yml")
                || filename.endsWith(".yaml")
                || filename.endsWith(".gradle")
                || filename.endsWith(".kts")
                || filename.endsWith(".xml")
                || filename.endsWith(".md");
    }

    private String limit(final String content) {
        int maxLength = 12_000;

        if (content == null) {
            return "";
        }

        if (content.length() <= maxLength) {
            return content;
        }

        return content.substring(0, maxLength)
                + "\n\n// ... content truncated for review context ...";
    }
}
