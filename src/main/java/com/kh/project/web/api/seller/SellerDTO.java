package com.kh.project.web.api.seller;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.time.LocalDate;

public class SellerDTO {

  @Data
  public static class JoinRequest {
    @NotBlank(message = "이메일은 필수 입력 항목입니다.")
    @Email(message = "유효한 이메일 형식이 아닙니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
    @Size(min = 8, max = 15, message = "비밀번호는 8~15자 사이여야 합니다.")
    private String password;

    @NotBlank(message = "사업자등록번호는 필수 입력 항목입니다.")
    private String bizRegNo;

    @NotBlank(message = "상호명은 필수 입력 항목입니다.")
    @Size(min = 2, max = 30, message = "가게 이름은 2자 이상 30자 이하로 입력해주세요.")
    private String shopName;

    @NotBlank(message = "대표자명은 필수 입력 항목입니다.")
    private String name;

    private String shopAddress;
    private String tel;
    private LocalDate birth;
  }

  @Data
  public static class InformationResponse {
    private Long sellerId;
    private String email;
    private String bizRegNo;
    private String shopName;
    private String name;
    private String shopAddress;
    private String tel;
    private LocalDate birth;
  }

  @Data
  public static class UpdateRequest {
    private String shopName;
    private String name;
    private String shopAddress;
    private String tel;
    private String currentPassword;
  }

  @Data
  public static class PasswordCheckRequest {
    private String password;
  }
}
