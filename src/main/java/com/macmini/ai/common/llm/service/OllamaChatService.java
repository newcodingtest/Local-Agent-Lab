package com.macmini.ai.common.llm.service;

import com.macmini.ai.common.llm.model.LlmModelProfile;
import com.macmini.ai.common.llm.model.LlmRequest;
import com.macmini.ai.common.llm.model.LlmResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.api.OllamaChatOptions;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OllamaChatService {

    private final ChatClient.Builder chatClientBuilder;

    private final LlmModelRouter llmModelRouter;

    public LlmResponse chat(final LlmRequest request) {
        validate(request);

        LlmModelProfile profile = llmModelRouter.route(request.getTaskType());

        String content = chatClientBuilder.build()
                .prompt()
                .system(profile.getSystemPrompt())
                .user(request.getPrompt())
                .options(OllamaChatOptions.builder()
                        .model(profile.getModel())
                        .temperature(profile.getTemperature())
                        .numPredict(profile.getNumPredict())
                        .build())
                .call()
                .content();

        return LlmResponse.builder()
                .taskType(request.getTaskType())
                .model(profile.getModel())
                .content(content)
                .build();
    }

    private void validate(final LlmRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("request must not be null");
        }

        if (request.getTaskType() == null) {
            throw new IllegalArgumentException("taskType must not be null");
        }

        if (request.getPrompt() == null || request.getPrompt().isBlank()) {
            throw new IllegalArgumentException("prompt must not be blank");
        }
    }
}
