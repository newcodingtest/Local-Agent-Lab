package com.macmini.ai.code.review.model;

public record ChangedFileContext(
        String path,
        String status,
        String patch,
        String content
) {
}
