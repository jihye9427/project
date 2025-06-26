package com.kh.project.domain.exception;

import com.kh.project.domain.common.ApiResponseCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
  private final ApiResponseCode responseCode;

  public BusinessException(ApiResponseCode responseCode) {
    super(responseCode.getRtmsg());
    this.responseCode = responseCode;
  }

  public BusinessException(ApiResponseCode responseCode, String message) {
    super(message);
    this.responseCode = responseCode;
  }
}
