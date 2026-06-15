package com.macmini.ai.code.review.model;

public record RelatedFileContext(
        String path,
        String reason,
        String content
) {
}
