package com.macmini.ai.code.review.service;

import com.macmini.ai.code.review.config.GithubProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Component
@RequiredArgsConstructor
public class GithubSignatureVerifier {

    private final GithubProperties githubProperties;

    public void verify(final String payload, final String signatureHeader){
        if (signatureHeader == null || signatureHeader.isBlank()){
            throw new IllegalArgumentException("Missing X-Hub-Signature-256");
        }

        String expected = "sha256=" + hmacSha256(payload, githubProperties.getWebhookSecret());

        boolean isValid = MessageDigest.isEqual(
                expected.getBytes(StandardCharsets.UTF_8),
                signatureHeader.getBytes(StandardCharsets.UTF_8)
        );

        if (!isValid){
            throw new IllegalArgumentException("Invalid GitHub webhook signature");
        }
    }

    private String hmacSha256(final String payload, final String secret){
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));

            byte[] bytes = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }

            return sb.toString();
        } catch (Exception e) {
            throw new IllegalStateException("Failed to verify Github signature", e);
        }
    }
}
