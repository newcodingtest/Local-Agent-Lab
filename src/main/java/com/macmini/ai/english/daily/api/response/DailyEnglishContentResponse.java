package com.macmini.ai.english.daily.api.response;

import com.macmini.ai.english.daily.model.DailyContentType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DailyEnglishContentResponse {

    private DailyContentType contentType;

    private String draft;

    private String review;

    private String validatedJson;

    private String finalContent;
}
