package com.macmini.ai.code.review.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.macmini.ai.code.review.service.GithubSignatureVerifier;
import com.macmini.ai.code.review.service.PullRequestReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/webhooks/github")
public class GithubWebHookApi {

    private final ObjectMapper objectMapper;
    private final GithubSignatureVerifier signatureVerifier;
    private final PullRequestReviewService pullRequestReviewService;

    @PostMapping
    public ResponseEntity<String> handle(
            @RequestHeader("X-GitHub-Event") String event,
            @RequestHeader(value = "X-Hub-Signature-256", required = false) String signature,
            @RequestBody String payload
    ) throws Exception {
        //test
        signatureVerifier.verify(payload, signature);

        if (!"pull_request".equals(event)) {
            return ResponseEntity.ok("ignored");
        }

        JsonNode root = objectMapper.readTree(payload);
        String action = root.path("action").asText();

        if (!action.equals("opened") && !action.equals("synchronize") && !action.equals("reopened")) {
            return ResponseEntity.ok("ignored");
        }

        pullRequestReviewService.review(root);

        return ResponseEntity.ok("reviewed");
    }
}
