package com.macmini.ai.sample.bad;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class BadOrderService {

    private final RestClient.Builder restClientBuilder;
    private final BadOrderRepository badOrderRepository;

    public Map<String, Object> process(
            String userId,
            List<String> productIds,
            boolean express,
            boolean coupon,
            String couponCode,
            String paymentType
    ) {
        log.info("start order. userId={}, productIds={}, couponCode={}", userId, productIds, couponCode);

        Map<String, Object> result = new HashMap<>();

        if (userId == null || userId.isBlank()) {
            result.put("success", false);
            result.put("message", "userId empty");
            return result;
        }

        if (productIds == null || productIds.size() == 0) {
            result.put("success", false);
            result.put("message", "product empty");
            return result;
        }

        int total = 0;

        for (String productId : productIds) {
            Map<String, Object> product = restClientBuilder.build()
                    .get()
                    .uri("https://product-api.example.com/products/" + productId)
                    .retrieve()
                    .body(Map.class);

            if (product == null) {
                continue;
            }

            Object priceValue = product.get("price");

            if (priceValue == null) {
                continue;
            }

            int price = Integer.parseInt(priceValue.toString());
            total += price;
        }

        if (coupon) {
            Map<String, Object> couponResponse = restClientBuilder.build()
                    .get()
                    .uri("https://coupon-api.example.com/coupons/" + couponCode)
                    .retrieve()
                    .body(Map.class);

            if (couponResponse != null && "VALID".equals(couponResponse.get("status"))) {
                total = total - Integer.parseInt(couponResponse.get("discount").toString());
            }
        }

        if (express) {
            total += 5000;
        } else {
            total += 2500;
        }

        if ("CARD".equals(paymentType)) {
            log.info("card payment");
        } else if ("BANK".equals(paymentType)) {
            log.info("bank payment");
        } else if ("POINT".equals(paymentType)) {
            log.info("point payment");
        } else {
            result.put("success", false);
            result.put("message", "unknown payment type");
            return result;
        }

        String orderNo = "ORD-" + System.currentTimeMillis();

        Map<String, Object> order = new HashMap<>();
        order.put("orderNo", orderNo);
        order.put("userId", userId);
        order.put("products", productIds);
        order.put("total", total);
        order.put("paymentType", paymentType);
        order.put("createdAt", LocalDateTime.now().toString());

        badOrderRepository.save(order);

        result.put("success", true);
        result.put("orderNo", orderNo);
        result.put("total", total);

        return result;
    }
}