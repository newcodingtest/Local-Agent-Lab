package com.macmini.ai.code.review.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GithubPullRequestFile(
        String filename,
        String status,
        String patch,
        Integer additions,
        Integer deletions,
        Integer changes,
        @JsonProperty("raw_url")
        String rawUrl
) {
}
