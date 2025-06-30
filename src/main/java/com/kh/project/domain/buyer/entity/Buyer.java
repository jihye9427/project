package com.kh.project.domain.buyer.entity;

import lombok.Data;
import java.sql.Timestamp;
import java.time.LocalDate;

@Data
public class Buyer {
  private Long buyerId;
  private String email;
  private String password;
  private String tel;
  private String name;
  private String nickname;
  private String gender;
  private LocalDate birth;
  private String address;
  private String gubun;
  private String status;
  private Timestamp cdate;
  private Timestamp udate;

  /**
   * 로그인 가능 여부 확인
   * @return 로그인 가능하면 true, 불가능하면 false
   */
  public boolean canLogin() {
    // status가 null이거나 빈 문자열인 경우 로그인 불가
    if (status == null || status.trim().isEmpty()) {
      return false;
    }

    // "활성" 상태만 로그인 가능
    // 다른 상태들: "탈퇴", "정지", "휴면" 등은 로그인 불가
    return "활성".equals(status.trim());
  }

  /**
   * 계정 상태 확인을 위한 메소드
   */
  public boolean isActive() {
    return "활성".equals(status);
  }

  public boolean isWithdrawn() {
    return "탈퇴".equals(status);
  }

  public boolean isSuspended() {
    return "정지".equals(status);
  }

  public boolean isDormant() {
    return "휴면".equals(status);
  }
}
