package com.kh.project.web.api.exception;

import com.kh.project.domain.common.ApiResponse;
import com.kh.project.domain.common.ApiResponseCode;
import com.kh.project.domain.exception.BusinessException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice(basePackages = "com.kh.project.web.api") // API 컨트롤러에만 적용
public class GlobalExceptionHandler {

    // 직접 정의한 BusinessException 처리
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 클라이언트 요청 오류이므로 400 응답
    public ApiResponse<Object> handleBusinessException(BusinessException e) {
        log.warn("Business Exception occurred: {}", e.getMessage());
        return ApiResponse.from(e); // 예외에 담긴 코드로 응답
    }

    // 예상치 못한 모든 서버 오류 처리
    @Valid
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        log.warn("Validation errors occurred: {}", errors);
        // ApiResponse의 withDetails 메소드를 사용하여 상세 오류 내역 반환 (새로 정의하거나 수정 필요)
        return ApiResponse.withDetails(ApiResponseCode.VALIDATION_ERROR, errors, null);
    }
}