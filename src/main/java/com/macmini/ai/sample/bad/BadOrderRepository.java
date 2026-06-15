package com.macmini.ai.sample.bad;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Slf4j
@Repository
public class BadOrderRepository {

    public void save(Map<String, Object> order) {
        log.info("save order: {}", order);
    }
}
