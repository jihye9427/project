package com.kh.project.domain.buyer.entity;

import lombok.Data;
import java.sql.Timestamp;
import java.util.Date;

@Data
public class Buyer {
  private Long buyerId;
  private String email;
  private String password;
  private String tel;
  private String name;
  private String nickname;
  private String gender;
  private Date birth;
  private String address;
  private String gubun;
  private String status;
  private Timestamp cdate;
  private Timestamp udate;
}
