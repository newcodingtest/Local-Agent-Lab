package com.macmini.ai.code.review.service;

import com.macmini.ai.code.review.model.ChangedFileContext;
import com.macmini.ai.code.review.model.ReviewDocumentContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ContextSelectionService {

    private static final int MAX_DOCUMENTS = 10;

    public List<ReviewDocumentContext> select(
            final List<ReviewDocumentContext> documents,
            final List<ChangedFileContext> changedFiles,
            final String diffText
    ) {
        List<ReviewDocumentContext> selected = new ArrayList<>();

        addIfPresent(documents, selected, "PROJECT_PROFILE");
        addIfPresent(documents, selected, "REVIEW_FOCUS");
        addIfPresent(documents, selected, "REVIEW_IGNORE");
        addIfPresent(documents, selected, "ARCHITECTURE_RULES");
        addIfPresent(documents, selected, "PACKAGE_ROLE_MAP");
        addIfPresent(documents, selected, "CLASS_ROLE_RULES");

        if (containsAny(diffText, "@Transactional", "Repository", "save(", "delete(", "update")) {
            addIfPresent(documents, selected, "TRANSACTION_POLICY");
        }

        if (containsAny(diffText, "Exception", "try", "catch", "Optional", "orElseThrow")) {
            addIfPresent(documents, selected, "ERROR_HANDLING_POLICY");
        }

        if (containsAny(diffText, "password", "token", "secret", "auth", "Security", "Jwt")) {
            addIfPresent(documents, selected, "SECURITY_POLICY");
        }

        if (containsAny(diffText, "for (", "stream()", "findAll", "Pageable", "cache", "Async")) {
            addIfPresent(documents, selected, "PERFORMANCE_POLICY");
        }

        addIfPresent(documents, selected, "TESTING_POLICY");
        addIfPresent(documents, selected, "README");
        addIfPresent(documents, selected, "BUILD_FILE");

        return selected.stream()
                .limit(MAX_DOCUMENTS)
                .toList();
    }

    private void addIfPresent(
            final List<ReviewDocumentContext> documents,
            final List<ReviewDocumentContext> selected,
            final String type
    ) {
        documents.stream()
                .filter(document -> type.equals(document.type()))
                .findFirst()
                .ifPresent(document -> {
                    if (!selected.contains(document)) {
                        selected.add(document);
                    }
                });
    }

    private boolean containsAny(final String text, final String... keywords) {
        if (text == null || text.isBlank()) {
            return false;
        }

        for (String keyword : keywords) {
            if (text.contains(keyword)) {
                return true;
            }
        }

        return false;
    }
}
