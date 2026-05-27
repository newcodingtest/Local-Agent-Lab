package com.macmini.ai.common.llm.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LlmRequest {

    private final LlmTaskType taskType;

    private final String prompt;
}
