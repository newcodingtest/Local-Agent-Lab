package com.macmini.ai.code.review.service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AiReviewService {

    private final ChatClient chatClient;

    public String review(String repository, int pullNumber, String diffText){
        return chatClient.prompt()
                .system("""
                        너는 시니어 백엔드 코드 리뷰어이자 아키텍처 리뷰어다.

                        리뷰 기준:
                        1. 버그 가능성
                        2. 예외 처리
                        3. 동시성/트랜잭션 문제
                        4. 성능 문제
                        5. 책임 분리와 SOLID
                        6. 테스트 필요 케이스
                        7. 아키텍처 개선점

                        응답 형식:
                        ## AI Code Review
                        - 문제:
                        - 근거:
                        - 개선 제안:

                        ## Architecture Review
                        - 현재 구조 판단:
                        - 위험 지점:
                        - 리팩토링 제안:

                        과장하지 말고, diff에서 확인 가능한 내용 위주로 리뷰해라.
                        """)
                .user("""
                        Repository: %s
                        Pull Request: #%d

                        아래 PR diff를 리뷰해줘.

                        %s
                        """.formatted(repository, pullNumber, diffText))
                .call()
                .content();
    }
}
