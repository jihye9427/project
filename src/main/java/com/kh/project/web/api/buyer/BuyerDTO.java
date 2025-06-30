package com.kh.project.web.api.buyer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;


public class BuyerDTO {

  @Data
  public static class JoinRequest {
    @NotBlank(message = "이메일은 필수 입력 항목입니다.")
    @Email(message = "유효한 이메일 형식이 아닙니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
    @Size(min = 8, max = 15, message = "비밀번호는 8~15자 사이여야 합니다.")
    private String password;

    private String tel;

    @NotBlank
    private String name;

    @NotBlank
    @Size(max = 8, message = "닉네임은 최대 8자까지 가능합니다.")
    private String nickname;

    private String gender;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birth;
    private String address;

    private String gubun;
  }

  @Data
  public static class InformationResponse {
    private Long buyerId;
    private String email;
    private String tel;
    private String name;
    private String nickname;
    private String gender;
    private LocalDate birth;
    private String address;
    private String status;
  }

  @Data
  public static class UpdateRequest {
    @NotBlank(message = "현재 비밀번호는 필수입니다.")
    private String currentPassword;

    @Size(min = 8, max = 15, message = "새 비밀번호는 8~15자 사이여야 합니다.")
    private String newPassword; // 새 비밀번호 필드 추가

    private String tel;

    @Size(max = 8, message = "닉네임은 최대 8자까지 가능합니다.")
    private String nickname;

    private String address;
  }

  @Data
  public static class PasswordCheckReq {
    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;
  }

  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class JoinResponse {
    private Long buyerId;
    private String email;
    private String name;
  }
}