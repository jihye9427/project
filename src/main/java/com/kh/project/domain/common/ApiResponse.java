package com.kh.project.domain.common;

import lombok.Getter;
import lombok.ToString;
import com.kh.project.domain.exception.BusinessException;

import java.util.Map;

@Getter
@ToString
// 공통 API 응답 클래스
public class ApiResponse<T> {
    private final boolean success; // 성공 여부
    private final T data; // 응답 데이터
    private final String message; // 메시지
    private final String errorCode; // 에러 코드
    private final Paging paging; // 페이징 정보(선택)
    private final Map<String, String> details; // 상세 정보(선택)

    /**
     * 생성자 (내부에서만 사용)
     */
    private ApiResponse(boolean success, T data, String message, String errorCode, Paging paging, Map<String, String> details) {
        this.success = success;
        this.data = data;
        this.message = message;
        this.errorCode = errorCode;
        this.paging = paging;
        this.details = details;
    }

    /**
     * 성공 응답 생성
     */
    public static <T> ApiResponse<T> createSuccess(T data, String message) {
        return new ApiResponse<>(true, data, message, null, null, null);
    }

    /**
     * 에러 응답 생성
     */
    public static <T> ApiResponse<T> createError(String message) {
        return new ApiResponse<>(false, null, message, "ERROR", null, null);
    }

    /**
     * 코드기반 응답 생성
     */
    public static <T> ApiResponse<T> of(ApiResponseCode code, T data) {
        boolean success = code == ApiResponseCode.SUCCESS;
        return new ApiResponse<>(success, data, code.getRtmsg(), code.getRtcd(), null, null);
    }

    /**
     * 페이징 응답 생성
     */
    public static <T> ApiResponse<T> of(ApiResponseCode code, T data, Paging paging) {
        boolean success = code == ApiResponseCode.SUCCESS;
        return new ApiResponse<>(success, data, code.getRtmsg(), code.getRtcd(), paging, null);
    }

    /**
     * 상세정보 응답 생성
     */
    public static <T> ApiResponse<T> withDetails(ApiResponseCode code, Map<String, String> details, T data) {
        boolean success = code == ApiResponseCode.SUCCESS;
        return new ApiResponse<>(success, data, code.getRtmsg(), code.getRtcd(), null, details);
    }

    /**
     * BusinessException 으로부터 에러 응답 생성
     */
    public static <T> ApiResponse<T> from(BusinessException e) {
        return new ApiResponse<>(false, null, e.getMessage(), e.getResponseCode().getRtcd(), null, null);
    }

    /**
     * 페이징 정보
     */
    @Getter
    @ToString
    public static class Paging {
        private final int numOfRows; // 페이지당 행 수
        private final int pageNo; // 페이지 번호
        private final int totalCount; // 전체 건수

        public Paging(int pageNo, int numOfRows, int totalCount) {
            this.pageNo = pageNo;
            this.numOfRows = numOfRows;
            this.totalCount = totalCount;
        }
    }
} 