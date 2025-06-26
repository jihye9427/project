package com.kh.project.domain.buyer.svc;

import com.kh.project.domain.buyer.entity.Buyer;
import com.kh.project.domain.exception.UserException.LoginFailed;

import java.util.Optional;

public interface BuyerSVC {
  Buyer join(Buyer buyer);
  Buyer login(String email, String password) throws LoginFailed;
  Optional<Buyer> findMemberInfo(Long buyerId);
  int updateMemberInfo(Long buyerId, Buyer buyer);
  int deleteMember(Long buyerId);
  boolean isExistEmail(String email);
  boolean checkPassword(Long buyerId, String password);
}
