package com.macmini.ai.code.review.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GithubContentResponse {

    /**
     * 파일명
     */
    private String name;

    /**
     * 파일 경로
     */
    private String path;

    /**
     * Git Blob SHA
     */
    private String sha;

    /**
     * 파일 타입
     * ex) file, dir
     */
    private String type;

    /**
     * Base64 인코딩된 파일 내용
     */
    private String content;

    /**
     * content 인코딩 방식
     * ex) base64
     */
    private String encoding;
}
