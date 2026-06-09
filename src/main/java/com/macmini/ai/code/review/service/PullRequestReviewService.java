package com.macmini.ai.code.review.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PullRequestReviewService {

    private static final int MAX_DIFF_CHARS = 50_000;

    private final GithubClient githubClient;
    private final AiReviewService aiReviewService;

    public void review(JsonNode payload) {
        String owner = payload.path("repository").path("owner").path("login").asText();
        String repo = payload.path("repository").path("name").asText();
        String fullName = payload.path("repository").path("full_name").asText();

        // 중요: PR 번호는 top-level number 사용
        int pullNumber = payload.path("number").asInt();

        log.info("Review target. owner={}, repo={}, fullName={}, pullNumber={}",
                owner, repo, fullName, pullNumber);
        //test
        JsonNode files = githubClient.getPullRequestFiles(owner, repo, pullNumber);

        String diffText = buildDiffText(files);

        if (diffText.isBlank()) {
            log.info("AI 리뷰 대상 diff가 없습니다.");
            githubClient.createIssueComment(owner, repo, pullNumber, "AI 리뷰 대상 diff가 없습니다.");
            return;
        }

        log.info("diffText: {}", diffText);
        String review = aiReviewService.review(fullName, pullNumber, diffText);

        githubClient.createIssueComment(owner, repo, pullNumber, review);
    }

    private String buildDiffText(JsonNode files) {
        StringBuilder sb = new StringBuilder();

        for (JsonNode file : files) {
            String filename = file.path("filename").asText();
            String status = file.path("status").asText();
            String patch = file.path("patch").asText("");

            if (patch.isBlank()) {
                continue;
            }

            sb.append("\n\n")
                    .append("### File: ").append(filename).append("\n")
                    .append("Status: ").append(status).append("\n")
                    .append("```diff\n")
                    .append(patch)
                    .append("\n```");

            if (sb.length() > MAX_DIFF_CHARS) {
                return sb.substring(0, MAX_DIFF_CHARS)
                        + "\n\n[Diff가 길어 일부만 리뷰했습니다.]";
            }
        }

        return sb.toString();
    }
}
