package com.kh.project.domain.seller.entity;

import lombok.Data;
import java.sql.Timestamp;
import java.util.Date;

@Data
public class Seller {
  private Long sellerId;
  private String email;
  private String password;
  private String bizRegNo;
  private String shopName;
  private String name;
  private String shopAddress;
  private String tel;
  private Date birth;
  private String status;
  private Timestamp cdate;
  private Timestamp udate;

  /**
   * 로그인 가능 여부 확인
   * @return 로그인 가능하면 true, 불가능하면 false
   */
  public boolean canLogin() {
    if (status == null || status.trim().isEmpty()) {
      return false;
    }
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
