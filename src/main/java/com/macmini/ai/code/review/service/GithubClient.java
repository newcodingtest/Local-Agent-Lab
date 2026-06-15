package com.macmini.ai.code.review.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.macmini.ai.code.review.config.GithubProperties;
import com.macmini.ai.code.review.model.GithubPullRequestFile;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

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

    public List<GithubPullRequestFile> getPullRequestFiles(
            final String owner,
            final String repo,
            final int pullNumber
    ) {
        return client()
                .get()
                .uri("/repos/{owner}/{repo}/pulls/{pullNumber}/files", owner, repo, pullNumber)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

    //test
    public void createIssueComment(final String owner, final String repo, final int issueNumber, String body){
        if (body == null || body.isBlank() || body.isEmpty()) {
            body = "AI 리뷰 결과가 비어 있습니다.";
        }

        client().post()
                .uri("/repos/{owner}/{repo}/issues/{pullNumber}/comments", owner, repo, issueNumber)
                .body(new CommentRequest(body))
                .retrieve()
                .toBodilessEntity();
    }

    private record CommentRequest(String body) {
    }

    public Optional<String> getFileContent(
            String owner,
            String repo,
            String path,
            String ref
    ) {
        try {
            GithubContentResponse response = client().get()
                    .uri("/repos/{owner}/{repo}/contents/{path}?ref={ref}",
                            owner, repo, path, ref)
                    .retrieve()
                    .body(GithubContentResponse.class);

            if (response == null || response.getContent() == null) {
                return Optional.empty();
            }

            String normalized = response.getContent().replace("\n", "");
            byte[] decoded = Base64.getDecoder().decode(normalized);

            return Optional.of(new String(decoded, StandardCharsets.UTF_8));
        } catch (HttpClientErrorException.NotFound e) {
            return Optional.empty();
        }
    }

    public Optional<String> getAgentsMd(
            final String owner,
            final String repo,
            final String ref
    ) {

        try {

            GithubContentResponse response =
                    client().get()
                            .uri(
                                    "/repos/{owner}/{repo}/contents/AGENTS.md?ref={ref}",
                                    owner,
                                    repo,
                                    ref
                            )
                            .retrieve()
                            .body(GithubContentResponse.class);

            if (response == null) {
                return Optional.empty();
            }

            return Optional.of(
                    GithubContentDecoder.decode(
                            response.getContent()
                    )
            );

        } catch (HttpClientErrorException.NotFound e) {

            return Optional.empty();
        }
    }
}
