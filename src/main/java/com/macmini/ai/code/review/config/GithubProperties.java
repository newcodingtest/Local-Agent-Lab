package com.macmini.ai.code.review.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "github")
public class GithubProperties {
    private String token;
    private String webhookSecret;
}
