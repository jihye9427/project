package com.kh.project.domain.seller.svc;

import com.kh.project.domain.exception.BusinessException;
import com.kh.project.domain.seller.dao.SellerDAO;
import com.kh.project.domain.seller.entity.Seller;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class SellerSVCImpl implements SellerSVC {

  private final SellerDAO sellerDAO;

  @Override
  public Seller join(Seller seller) {
    if (sellerDAO.isExistEmail(seller.getEmail())) {
      throw new BusinessException("이미 사용중인 이메일입니다.");
    }
    return sellerDAO.save(seller);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<Seller> login(String email, String password) {
    return sellerDAO.findByEmail(email)
        .filter(seller -> seller.getPassword().equals(password));
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<Seller> findMemberInfo(Long sellerId) {
    return sellerDAO.findById(sellerId);
  }

  @Override
  public int updateMemberInfo(Long sellerId, Seller seller) {
    return sellerDAO.update(sellerId, seller);
  }

  @Override
  public int deleteMember(Long sellerId) {
    return sellerDAO.delete(sellerId);
  }

  @Override
  @Transactional(readOnly = true)
  public boolean isExistEmail(String email) {
    return sellerDAO.isExistEmail(email);
  }

  @Override
  @Transactional(readOnly = true)
  public boolean checkPassword(Long sellerId, String password) {
    return sellerDAO.findById(sellerId)
        .map(seller -> seller.getPassword().equals(password))
        .orElse(false);
  }
}