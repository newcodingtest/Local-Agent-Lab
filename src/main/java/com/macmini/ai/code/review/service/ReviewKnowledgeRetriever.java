package com.macmini.ai.code.review.service;

import com.macmini.ai.code.review.model.ChangedFileContext;
import com.macmini.ai.code.review.model.RetrievedReviewKnowledge;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewKnowledgeRetriever {

    public List<RetrievedReviewKnowledge> retrieve(
            final String diffText,
            final List<ChangedFileContext> changedFiles
    ) {
        List<RetrievedReviewKnowledge> result = new ArrayList<>();

        result.add(cleanCodeMethodRules());
        result.add(cleanCodeClassRules());
        result.add(cleanArchitectureLayerRules());
        result.add(cleanArchitectureDependencyRules());
        result.add(cleanArchitectureTestabilityRules());

        return result;
    }

    private RetrievedReviewKnowledge cleanCodeMethodRules() {
        return new RetrievedReviewKnowledge(
                "CLEAN_CODE_METHOD_RULES.md",
                "Clean Code - Method Rules",
                """
                # Clean Code Method Rules

                ## Review Focus
                - 메서드는 한 가지 일만 해야 한다.
                - 메서드 이름은 내부 구현이 아니라 의도를 드러내야 한다.
                - 조건문이 깊어지면 정책 객체, 전략, 분기 메서드 분리를 검토한다.
                - 같은 추상화 수준의 문장들이 한 메서드 안에 있어야 한다.
                - 단순히 라인 수를 줄이기 위한 private method 분리는 피한다.
                - private method는 의미 있는 도메인 개념을 가져야 한다.

                ## Detect
                - 하나의 메서드에서 검증, 조회, 변환, 저장, 외부 호출을 모두 수행한다.
                - if/else가 중첩되어 읽기 어렵다.
                - boolean flag 인자가 메서드 동작을 크게 바꾼다.
                - 메서드명이 process, handle, doSomething처럼 모호하다.

                ## Review
                - 책임 단위로 메서드를 분리한다.
                - 분기 조건에 이름을 부여한다.
                - 정책 판단과 실행 로직을 분리한다.
                - 도메인 의미가 드러나는 이름으로 변경한다.
                """
        );
    }

    private RetrievedReviewKnowledge cleanCodeClassRules() {
        return new RetrievedReviewKnowledge(
                "CLEAN_CODE_CLASS_RULES.md",
                "Clean Code - Class Rules",
                """
                # Clean Code Class Rules

                ## Review Focus
                - 클래스는 하나의 책임을 가져야 한다.
                - 클래스 이름과 실제 책임이 일치해야 한다.
                - 너무 많은 의존성을 가진 클래스는 책임이 비대할 가능성이 높다.
                - 데이터 변환, 정책 판단, 외부 연동, 저장 책임이 한 클래스에 섞이면 분리한다.

                ## Detect
                - Service 클래스가 지나치게 많은 private method를 가진다.
                - Controller 또는 Api 클래스가 비즈니스 판단을 수행한다.
                - Repository가 비즈니스 규칙을 포함한다.
                - Util 클래스가 도메인 로직을 숨긴다.

                ## Review
                - Orchestrator, Policy, Resolver, Factory, Client, Repository 책임을 분리한다.
                - 클래스 이름이 책임을 정확히 표현하도록 변경한다.
                - 하나의 변경 이유만 갖도록 구조를 조정한다.
                """
        );
    }

    private RetrievedReviewKnowledge cleanArchitectureLayerRules() {
        return new RetrievedReviewKnowledge(
                "CLEAN_ARCHITECTURE_LAYER_RULES.md",
                "Clean Architecture - Layer Rules",
                """
                # Clean Architecture Layer Rules

                ## Review Focus
                - 상위 정책은 하위 구현 세부사항에 의존하면 안 된다.
                - Controller/API는 요청 파싱과 응답 변환에 집중한다.
                - Application Service는 유스케이스 흐름을 조율한다.
                - Domain Service는 핵심 도메인 규칙을 처리한다.
                - Infrastructure는 외부 API, DB, 파일, 메시징 세부사항을 담당한다.

                ## Detect
                - Controller에서 Repository를 직접 호출한다.
                - Domain 로직이 Web/API 객체에 의존한다.
                - Service 안에 HTTP, GitHub API, DB, LLM 호출 세부사항이 모두 섞여 있다.
                - Entity 또는 Domain 객체가 Spring Framework에 강하게 의존한다.

                ## Review
                - API, Application, Domain, Infrastructure 책임을 분리한다.
                - 외부 연동은 Client 또는 Adapter로 격리한다.
                - 유스케이스 흐름과 세부 구현을 분리한다.
                """
        );
    }

    private RetrievedReviewKnowledge cleanArchitectureDependencyRules() {
        return new RetrievedReviewKnowledge(
                "CLEAN_ARCHITECTURE_DEPENDENCY_RULES.md",
                "Clean Architecture - Dependency Rules",
                """
                # Clean Architecture Dependency Rules

                ## Review Focus
                - 의존성은 안쪽 정책 방향으로 향해야 한다.
                - 구체 구현보다 인터페이스 또는 역할에 의존한다.
                - 테스트하기 어려운 직접 의존성을 줄인다.
                - 외부 시스템 의존성은 경계 밖으로 밀어낸다.

                ## Detect
                - 핵심 서비스가 RestClient, WebClient, JDBC, GitHub API 모델에 직접 의존한다.
                - 비즈니스 판단 코드가 외부 응답 DTO 구조에 묶여 있다.
                - 생성자 의존성이 과도하게 많다.
                - 순환 의존성 가능성이 있다.

                ## Review
                - Port/Adapter 구조를 검토한다.
                - 외부 DTO를 내부 모델로 변환한다.
                - 역할 중심 인터페이스를 도입한다.
                - 의존성이 많은 클래스는 책임 분리를 검토한다.
                """
        );
    }

    private RetrievedReviewKnowledge cleanArchitectureTestabilityRules() {
        return new RetrievedReviewKnowledge(
                "CLEAN_ARCHITECTURE_TESTABILITY_RULES.md",
                "Clean Architecture - Testability Rules",
                """
                # Clean Architecture Testability Rules

                ## Review Focus
                - 핵심 로직은 Spring Context 없이 테스트 가능해야 한다.
                - 외부 API 호출과 판단 로직은 분리되어야 한다.
                - 시간, 랜덤, 네트워크, 파일 시스템은 주입 가능한 경계로 격리한다.
                - 테스트가 Mock 설정에 과도하게 의존하면 설계 문제가 숨어 있을 수 있다.

                ## Detect
                - 단위 테스트가 어려운 static 호출 또는 직접 new 사용이 많다.
                - 핵심 판단을 테스트하려면 외부 API Mock이 많이 필요하다.
                - private method만 많고 public behavior 검증이 어렵다.
                - 테스트가 구현 세부사항 verify에 치우쳐 있다.

                ## Review
                - 순수 판단 로직을 별도 컴포넌트로 분리한다.
                - 외부 호출은 Client/Adapter 뒤로 숨긴다.
                - 테스트는 결과와 상태 변화를 중심으로 작성한다.
                """
        );
    }
}