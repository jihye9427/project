package com.kh.project.domain.exception;

import com.kh.project.domain.common.ApiResponseCode;

/**
 * 사용자 관련 예외
 */
public class UserException extends BusinessException {
  /**
   * 부모 생성자. 중첩 클래스에서 호출하여 상태 코드와 메시지 설정
   * @param responseCode 응답 코드 Enum
   * @param message 응답 메시지
   */
  public UserException(ApiResponseCode responseCode, String message) {
    super(responseCode, message);
  }

  /**
   * 로그인 실패 예외
   * - 원인: 존재하지 않는 이메일 또는 잘못된 비밀번호 입력
   * - 응답 코드: U06 (LOGIN_FAILED)
   */
  public static class LoginFailed extends UserException {
    public LoginFailed(String message) {
      super(ApiResponseCode.LOGIN_FAILED, message);
    }
  }

  /**
   * 이메일 중복 예외
   * - 원인: 회원가입 시 이미 시스템에 등록된 이메일 주소 사용
   * - 응답 코드: U02 (USER_ALREADY_EXISTS)
   */
  public static class EmailAlreadyExists extends UserException {
    public EmailAlreadyExists(String message) {
      super(ApiResponseCode.USER_ALREADY_EXISTS, message);
    }
  }

  /**
   * 닉네임 중복 예외
   * - 원인: 회원가입 시 이미 시스템에 등록된 닉네임 사용
   * - 응답 코드: U04 (NICKNAME_ALREADY_EXISTS)
   */
  public static class NicknameAlreadyExists extends UserException {
    public NicknameAlreadyExists(String message) {
      super(ApiResponseCode.NICKNAME_ALREADY_EXISTS, message);
    }
  }

  /**
   * 사업자 등록번호 중복 예외
   * - 원인: 판매자 회원가입 시 이미 시스템에 등록된 사업자 번호 사용
   * - 응답 코드: U05 (BIZ_REG_NO_ALREADY_EXISTS)
   */
  public static class BizRegNoAlreadyExists extends UserException {
    public BizRegNoAlreadyExists(String message) {
      super(ApiResponseCode.BIZ_REG_NO_ALREADY_EXISTS, message);
    }
  }

  /**
   * 비밀번호 불일치 예외
   * - 원인: 회원 정보 수정, 회원 탈퇴 등 본인 확인 절차에서 현재 비밀번호를 잘못 입력
   * - 응답 코드: U03 (INVALID_PASSWORD)
   */
  public static class PasswordMismatched extends UserException {
    public PasswordMismatched(String message) {
      super(ApiResponseCode.INVALID_PASSWORD, message);
    }
  }
}