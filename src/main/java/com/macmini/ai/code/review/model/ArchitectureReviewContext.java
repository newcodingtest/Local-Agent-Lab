package com.macmini.ai.code.review.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ArchitectureReviewContext {

    private final String repository;
    private final int pullNumber;
    private final String diffText;
    private final String projectRules;
    private final String changedFileSources;
    private final String packageTree;
}