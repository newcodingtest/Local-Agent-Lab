package com.macmini.ai.code.review.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.macmini.ai.code.review.service.GithubSignatureVerifier;
import com.macmini.ai.code.review.service.PullRequestReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
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
        log.info("GitHub Event: {}", event);
        log.info("payload: {}", payload);

        signatureVerifier.verify(payload, signature);

        if ("ping".equals(event)) {
            log.info("GitHub webhook ping received");
            return ResponseEntity.ok("pong");
        }

        if (!"pull_request".equals(event)) {
            log.info("ignored event: {}", event);
            return ResponseEntity.ok("ignored event: " + event);
        }
        
        JsonNode root = objectMapper.readTree(payload);
        String action = root.path("action").asText();

        log.info("Pull request action: {}", action);

        if (!action.equals("opened")
                && !action.equals("synchronize")
                && !action.equals("reopened")) {
            return ResponseEntity.ok("ignored action: " + action);
        }

        pullRequestReviewService.review(root);

        return ResponseEntity.ok("reviewed");
    }
}
