package com.macmini.ai.code.review.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.macmini.ai.code.review.config.GithubProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class GithubClient {

    private final GithubProperties githubProperties;

    private RestClient client(){
        return RestClient.builder()
                .baseUrl("https://api.github.com")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + githubProperties.getToken())
                .defaultHeader(HttpHeaders.ACCEPT, "application/vnd.github+json")
                .defaultHeader("X-Github-Api-Version", "2022-11-28")
                .build();
    }

    public JsonNode getPullRequestFiles(final String owner, final String repo, final int pullNumber){
        return client().get()
                .uri("/repos/{owner}/{repo}/pulls/{pullNumber}/files", owner, repo, pullNumber)
                .retrieve()
                .body(JsonNode.class);
    }

    public void createIssueComment(final String owner, final String repo, final int issueNumber, final String body){
        client().post()
                .uri("/repos/{owner}/{repo}/issues/{pullNumber}/comments", owner, repo, issueNumber)
                .body(new CommentRequest(body))
                .retrieve()
                .toBodilessEntity();
    }

    private record CommentRequest(String body) {
    }
}
