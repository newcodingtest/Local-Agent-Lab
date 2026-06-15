package com.macmini.ai.code.review.model;

import java.util.List;

public record ReviewContext(
        String owner,
        String repo,
        String repository,
        int pullNumber,
        String baseBranch,
        String diffText,
        List<ChangedFileContext> changedFiles,
        List<RelatedFileContext> relatedFiles,
        List<TestFileContext> testFiles,
        List<RetrievedReviewKnowledge> knowledgeList
) {
}
