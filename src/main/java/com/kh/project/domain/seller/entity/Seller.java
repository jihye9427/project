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
}
