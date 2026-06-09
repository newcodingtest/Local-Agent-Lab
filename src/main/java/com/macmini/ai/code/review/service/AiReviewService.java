package com.macmini.ai.code.review.service;

import com.macmini.ai.common.llm.model.LlmModelProfile;
import com.macmini.ai.common.llm.model.LlmTaskType;
import com.macmini.ai.common.llm.service.LlmModelRouter;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import org.springframework.ai.ollama.api.OllamaChatOptions;

@Service
public class AiReviewService {

    private final ChatClient chatClient;
    private final LlmModelRouter llmModelRouter;

    public AiReviewService(
            final ChatClient.Builder chatClientBuilder,
            final LlmModelRouter llmModelRouter
    ) {
        this.chatClient = chatClientBuilder.build();
        this.llmModelRouter = llmModelRouter;
    }

    public String review(
            final String repository,
            final int pullNumber,
            final String diffText
    ) {
        String codeReview = reviewByTask(
                LlmTaskType.CODE_REVIEW,
                repository,
                pullNumber,
                diffText
        );

        String architectureReview = reviewByTask(
                LlmTaskType.ARCHITECTURE_REVIEW,
                repository,
                pullNumber,
                diffText
        );

        return """
                ## AI Code Review

                %s

                ---

                ## Architecture Review

                %s
                """.formatted(codeReview, architectureReview);
    }

    private String reviewByTask(
            final LlmTaskType taskType,
            final String repository,
            final int pullNumber,
            final String diffText
    ) {
        LlmModelProfile profile = llmModelRouter.route(taskType);

        return chatClient.prompt()
                .options(OllamaChatOptions.builder()
                        .model(profile.getModel())
                        .temperature(profile.getTemperature())
                        .numPredict(profile.getNumPredict())
                        .build())
                .system(profile.getSystemPrompt())
                .user("""
                        Repository: %s
                        Pull Request: #%d

                        아래 PR diff를 리뷰해줘.

                        %s
                        """.formatted(repository, pullNumber, diffText))
                .call()
                .content();
    }
}
