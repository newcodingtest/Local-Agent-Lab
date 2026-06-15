package com.macmini.ai.code.review.service;

import com.macmini.ai.code.review.model.GithubPullRequestFile;
import com.macmini.ai.code.review.model.ReviewContext;
import com.macmini.ai.common.llm.model.LlmModelProfile;
import com.macmini.ai.common.llm.model.LlmTaskType;
import com.macmini.ai.common.llm.service.LlmModelRouter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.api.OllamaChatOptions;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class AiReviewService {

    private final ChatClient chatClient;
    private final LlmModelRouter llmModelRouter;
    private final ReviewContextBuilder reviewContextBuilder;
    private final ReviewPromptFactory reviewPromptFactory;

    public AiReviewService(
            final ChatClient.Builder chatClientBuilder,
            final LlmModelRouter llmModelRouter,
            final ReviewContextBuilder reviewContextBuilder,
            final ReviewPromptFactory reviewPromptFactory
    ) {
        this.chatClient = chatClientBuilder.build();
        this.llmModelRouter = llmModelRouter;
        this.reviewContextBuilder = reviewContextBuilder;
        this.reviewPromptFactory = reviewPromptFactory;
    }

    public String review(
            final String owner,
            final String repo,
            final String repository,
            final int pullNumber,
            final String baseBranch,
            final String headRef,
            final String diffText,
            final List<GithubPullRequestFile> pullRequestFiles
    ) {
        ReviewContext context = reviewContextBuilder.build(
                owner,
                repo,
                repository,
                pullNumber,
                baseBranch,
                headRef,
                diffText,
                pullRequestFiles
        );

        String cleanCodeReview = reviewByTask(
                LlmTaskType.CODE_REVIEW,
                context
        );

        String cleanArchitectureReview = reviewByTask(
                LlmTaskType.ARCHITECTURE_REVIEW,
                context
        );

        return """
                ## AI Clean Code Review

                %s

                ---

                ## AI Clean Architecture Review

                %s
                """.formatted(cleanCodeReview, cleanArchitectureReview);
    }

    private String reviewByTask(
            final LlmTaskType taskType,
            final ReviewContext context
    ) {
        LlmModelProfile profile = llmModelRouter.route(taskType);

        String userPrompt = reviewPromptFactory.createUserPrompt(context);

        return chatClient.prompt()
                .options(OllamaChatOptions.builder()
                        .model(profile.getModel())
                        .temperature(profile.getTemperature())
                        .numPredict(profile.getNumPredict())
                        .build())
                .system(profile.getSystemPrompt())
                .user(userPrompt)
                .call()
                .content();
    }
}