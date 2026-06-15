package com.macmini.ai.code.review.service;

import com.macmini.ai.code.review.model.ChangedFileContext;
import com.macmini.ai.code.review.model.RelatedFileContext;
import com.macmini.ai.code.review.model.TestFileContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class RelatedFileResolver {

    private static final Pattern IMPORT_PATTERN =
            Pattern.compile("^import\\s+([a-zA-Z0-9_.]+);", Pattern.MULTILINE);

    private final GithubClient githubClient;

    public List<RelatedFileContext> resolve(
            final String owner,
            final String repo,
            final String ref,
            final List<ChangedFileContext> changedFiles
    ) {
        Set<String> changedPaths = changedFiles.stream()
                .map(ChangedFileContext::path)
                .collect(java.util.stream.Collectors.toSet());

        Set<String> candidatePaths = new LinkedHashSet<>();

        for (ChangedFileContext changedFile : changedFiles) {
            candidatePaths.addAll(resolveInternalImportPaths(changedFile));
            candidatePaths.addAll(resolveSiblingLikelyPaths(changedFile));
        }

        return candidatePaths.stream()
                .filter(path -> !changedPaths.contains(path))
                .limit(15)
                .map(path -> githubClient.getFileContent(owner, repo, path, ref)
                        .map(content -> new RelatedFileContext(
                                path,
                                "changed file import or same package candidate",
                                limit(content)
                        ))
                        .orElse(null))
                .filter(Objects::nonNull)
                .toList();
    }

    public List<TestFileContext> resolveTests(
            final String owner,
            final String repo,
            final String ref,
            final List<ChangedFileContext> changedFiles
    ) {
        Set<String> testPaths = new LinkedHashSet<>();

        for (ChangedFileContext file : changedFiles) {
            if (!file.path().startsWith("src/main/java/")) {
                continue;
            }

            String testPath = file.path()
                    .replace("src/main/java/", "src/test/java/")
                    .replace(".java", "Test.java");

            testPaths.add(testPath);
        }

        return testPaths.stream()
                .limit(10)
                .map(path -> githubClient.getFileContent(owner, repo, path, ref)
                        .map(content -> new TestFileContext(path, limit(content)))
                        .orElse(null))
                .filter(Objects::nonNull)
                .toList();
    }

    private List<String> resolveInternalImportPaths(final ChangedFileContext file) {
        if (!file.path().endsWith(".java")) {
            return List.of();
        }

        Matcher matcher = IMPORT_PATTERN.matcher(file.content());
        List<String> paths = new ArrayList<>();

        while (matcher.find()) {
            String importName = matcher.group(1);

            if (isExternalImport(importName)) {
                continue;
            }

            String path = "src/main/java/" + importName.replace(".", "/") + ".java";
            paths.add(path);
        }

        return paths;
    }

    private List<String> resolveSiblingLikelyPaths(final ChangedFileContext file) {
        String path = file.path();

        if (!path.endsWith(".java")) {
            return List.of();
        }

        String directory = path.substring(0, path.lastIndexOf("/") + 1);
        String className = path.substring(path.lastIndexOf("/") + 1, path.length() - ".java".length());

        List<String> candidates = new ArrayList<>();

        if (className.endsWith("Service")) {
            String baseName = className.substring(0, className.length() - "Service".length());
            candidates.add(directory + baseName + "Repository.java");
            candidates.add(directory + baseName + "Client.java");
            candidates.add(directory + baseName + "Mapper.java");
        }

        if (className.endsWith("Controller") || className.endsWith("Api")) {
            String baseName = className
                    .replace("Controller", "")
                    .replace("Api", "");
            candidates.add(directory.replace("/api/", "/service/") + baseName + "Service.java");
        }

        return candidates;
    }

    private boolean isExternalImport(final String importName) {
        return importName.startsWith("java.")
                || importName.startsWith("javax.")
                || importName.startsWith("jakarta.")
                || importName.startsWith("org.springframework.")
                || importName.startsWith("lombok.")
                || importName.startsWith("org.slf4j.")
                || importName.startsWith("com.fasterxml.")
                || importName.startsWith("org.junit.")
                || importName.startsWith("org.mockito.");
    }

    private String limit(final String content) {
        int maxLength = 10_000;

        if (content == null) {
            return "";
        }

        if (content.length() <= maxLength) {
            return content;
        }

        return content.substring(0, maxLength)
                + "\n\n// ... content truncated for review context ...";
    }
}