package com.macmini.ai.english.daily.api;

import com.macmini.ai.english.daily.api.request.DailyEnglishContentRequest;
import com.macmini.ai.english.daily.api.response.DailyEnglishContentResponse;
import com.macmini.ai.english.daily.service.DailyEnglishContentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/english/daily")
public class DailyEnglishContentApi {

    private final DailyEnglishContentService dailyEnglishContentService;

    @PostMapping("/generate")
    public DailyEnglishContentResponse generate(@RequestBody DailyEnglishContentRequest request) {
        DailyEnglishContentResponse response = dailyEnglishContentService.generate(request);
        log.info("response: {}", response);
        return dailyEnglishContentService.generate(request);
    }
}