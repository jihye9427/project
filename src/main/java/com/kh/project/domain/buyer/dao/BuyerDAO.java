package com.kh.project.domain.buyer.dao;

import com.kh.project.domain.buyer.entity.Buyer;
import java.util.Optional;

public interface BuyerDAO {
  Buyer save(Buyer buyer);
  Optional<Buyer> findByEmail(String email);
  Optional<Buyer> findById(Long buyerId);
  int update(Long buyerId, Buyer buyer);
  int delete(Long buyerId);
  boolean existsByEmail(String email);
  boolean existsByNickname(String nickname);
}
