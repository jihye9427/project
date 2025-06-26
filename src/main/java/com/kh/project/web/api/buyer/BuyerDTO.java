package com.kh.project.web.api.buyer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;


public class BuyerDTO {

  @Data
  public static class JoinReq {
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
    private Date birth;
    private String address;
  }

  @Data
  public static class InfoRes {
    private Long buyerId;
    private String email;
    private String tel;
    private String name;
    private String nickname;
    private String gender;
    private Date birth;
    private String address;
  }

  @Data
  public static class UpdateReq {
    private String tel;
    private String nickname;
    private String address;
    private String currentPassword;
  }

  @Data
  public static class PasswordCheckReq {
    private String password;
  }
}