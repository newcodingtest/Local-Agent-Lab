package com.macmini.ai.english.daily.service;

import com.macmini.ai.common.llm.model.LlmRequest;
import com.macmini.ai.common.llm.model.LlmResponse;
import com.macmini.ai.common.llm.model.LlmTaskType;
import com.macmini.ai.common.llm.service.OllamaChatService;
import com.macmini.ai.english.daily.api.request.DailyEnglishContentRequest;
import com.macmini.ai.english.daily.api.response.DailyEnglishContentResponse;
import com.macmini.ai.english.daily.prompt.DailyEnglishPromptBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DailyEnglishContentPipelineService {

    private final OllamaChatService ollamaChatService;

    private final DailyEnglishPromptBuilder promptBuilder;

    public DailyEnglishContentResponse generate(final DailyEnglishContentRequest request) {
        validate(request);

        LlmResponse draft = ollamaChatService.chat(
                LlmRequest.builder()
                        .taskType(LlmTaskType.CONTENT_GENERATION)
                        .prompt(promptBuilder.buildDraftPrompt(request))
                        .build()
        );

        LlmResponse review = ollamaChatService.chat(
                LlmRequest.builder()
                        .taskType(LlmTaskType.ENGLISH_REVIEW)
                        .prompt(promptBuilder.buildReviewPrompt(draft.getContent()))
                        .build()
        );

        LlmResponse validatedJson = ollamaChatService.chat(
                LlmRequest.builder()
                        .taskType(LlmTaskType.JSON_VALIDATE)
                        .prompt(promptBuilder.buildJsonValidationPrompt(draft.getContent()))
                        .build()
        );

        LlmResponse finalContent = ollamaChatService.chat(
                LlmRequest.builder()
                        .taskType(LlmTaskType.FINAL_REWRITE)
                        .prompt(promptBuilder.buildFinalRewritePrompt(
                                validatedJson.getContent(),
                                review.getContent()
                        ))
                        .build()
        );

        return DailyEnglishContentResponse.builder()
                .contentType(request.getContentType())
                .draft(draft.getContent())
                .review(review.getContent())
                .validatedJson(validatedJson.getContent())
                .finalContent(finalContent.getContent())
                .build();
    }

    private void validate(final DailyEnglishContentRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("request must not be null");
        }

        if (request.getContentType() == null) {
            throw new IllegalArgumentException("contentType must not be null");
        }

        if (request.getYear() == null || request.getMonth() == null || request.getDay() == null) {
            throw new IllegalArgumentException("date must not be null");
        }
    }
}
