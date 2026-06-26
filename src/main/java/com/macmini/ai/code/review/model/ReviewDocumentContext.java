package com.macmini.ai.code.review.model;

public record ReviewDocumentContext(
        String path,
        String type,
        String content
) {
}