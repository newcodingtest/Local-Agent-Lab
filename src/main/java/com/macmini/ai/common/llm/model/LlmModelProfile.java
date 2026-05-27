package com.macmini.ai.common.llm.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LlmModelProfile {

    private final String model;

    private final Double temperature;

    private final Integer numPredict;

    private final String systemPrompt;
}