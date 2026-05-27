package com.macmini.ai.english.daily.service;

import com.macmini.ai.english.daily.api.request.DailyEnglishContentRequest;
import com.macmini.ai.english.daily.api.response.DailyEnglishContentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DailyEnglishContentService {

    private final DailyEnglishContentPipelineService pipelineService;

    public DailyEnglishContentResponse generate(final DailyEnglishContentRequest request) {
        return pipelineService.generate(request);
    }
}
