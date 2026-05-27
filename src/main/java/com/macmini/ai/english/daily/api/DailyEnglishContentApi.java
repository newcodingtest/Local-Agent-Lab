package com.macmini.ai.english.daily.api;

import com.macmini.ai.english.daily.api.request.DailyEnglishContentRequest;
import com.macmini.ai.english.daily.api.response.DailyEnglishContentResponse;
import com.macmini.ai.english.daily.service.DailyEnglishContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/english/daily")
public class DailyEnglishContentApi {

    private final DailyEnglishContentService dailyEnglishContentService;

    @PostMapping("/generate")
    public DailyEnglishContentResponse generate(@RequestBody DailyEnglishContentRequest request) {
        return dailyEnglishContentService.generate(request);
    }
}