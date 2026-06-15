package com.macmini.ai.code.review.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.macmini.ai.code.review.model.GithubPullRequestFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PullRequestReviewService {

    private static final int MAX_DIFF_CHARS = 50_000;

    private final GithubClient githubClient;
    private final AiReviewService aiReviewService;

    public void review(final JsonNode payload) {
        String owner = payload.path("repository").path("owner").path("login").asText();
        String repo = payload.path("repository").path("name").asText();
        String repository = payload.path("repository").path("full_name").asText();
        int pullNumber = payload.path("pull_request").path("number").asInt();
        String headRef = payload.path("pull_request").path("head").path("sha").asText();
        String baseBranch = payload.path("pull_request").path("base").path("ref").asText();

        log.info(
                "Review target. owner={}, repo={}, fullName={}, pullNumber={}, headRef={}, baseBranch={}",
                owner,
                repo,
                repository,
                pullNumber,
                headRef,
                baseBranch
        );

        List<GithubPullRequestFile> files = githubClient.getPullRequestFiles(
                owner,
                repo,
                pullNumber
        );

        String diffText = files.stream()
                .map(this::formatDiff)
                .collect(Collectors.joining("\n\n"));

        if (diffText.isBlank()) {
            githubClient.createIssueComment(
                    owner,
                    repo,
                    pullNumber,
                    "AI 리뷰 대상 diff가 없습니다."
            );
            return;
        }

        String reviewComment = aiReviewService.review(
                owner,
                repo,
                repository,
                pullNumber,
                baseBranch,
                headRef,
                diffText,
                files
        );

        githubClient.createIssueComment(
                owner,
                repo,
                pullNumber,
                reviewComment
        );
    }

    private String formatDiff(final GithubPullRequestFile file) {
        return """
                ### File: %s
                Status: %s
                Additions: %d
                Deletions: %d

                ```diff
                %s
                ```
                """.formatted(
                file.filename(),
                file.status(),
                valueOrZero(file.additions()),
                valueOrZero(file.deletions()),
                file.patch() == null ? "" : file.patch()
        );
    }

    private int valueOrZero(final Integer value) {
        return value == null ? 0 : value;
    }

}
