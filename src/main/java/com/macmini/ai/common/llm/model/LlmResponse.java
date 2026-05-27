package com.macmini.ai.common.llm.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LlmResponse {

    private final LlmTaskType taskType;

    private final String model;

    private final String content;
}