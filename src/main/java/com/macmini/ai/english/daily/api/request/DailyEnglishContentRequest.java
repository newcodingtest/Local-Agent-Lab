package com.macmini.ai.english.daily.api.request;

import com.macmini.ai.english.daily.model.DailyContentType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DailyEnglishContentRequest {

    private DailyContentType contentType;

    private Integer year;

    private Integer month;

    private Integer day;

    private String topic;
}
