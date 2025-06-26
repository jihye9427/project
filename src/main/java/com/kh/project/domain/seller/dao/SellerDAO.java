package com.kh.project.domain.seller.dao;

import com.kh.project.domain.seller.entity.Seller;

import java.util.Optional;

public interface SellerDAO {
  Seller save(Seller seller);
  Optional<Seller> findByEmail(String email);
  Optional<Seller> findById(Long sellerId);
  int update(Long sellerId, Seller seller);
  int delete(Long sellerId);
  boolean existsByEmail(String email);
  boolean existsByBizRegNo(String bizRegNo);
}
