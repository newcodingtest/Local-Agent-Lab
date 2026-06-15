package com.macmini.ai.code.review.service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public final class GithubContentDecoder {

    private GithubContentDecoder() {
    }

    /**
     * GitHub Contents API의 Base64 content를 UTF-8 문자열로 변환
     */
    public static String decode(final String encodedContent) {

        if (encodedContent == null || encodedContent.isBlank()) {
            return "";
        }

        String normalized = encodedContent.replaceAll("\\s+", "");

        byte[] decoded =
                Base64.getDecoder().decode(normalized);

        return new String(decoded, StandardCharsets.UTF_8);
    }
}