package com.kh.project.domain.seller.svc;

import com.kh.project.domain.seller.entity.Seller;

import java.util.Optional;


public interface SellerSVC {
  Seller join(Seller seller);
  Optional<Seller> login(String email, String password);
  Optional<Seller> findMemberInfo(Long sellerId);
  int updateMemberInfo(Long sellerId, Seller seller);
  int deleteMember(Long sellerId);
  boolean isExistEmail(String email);
  boolean checkPassword(Long sellerId, String password);
}
