package com.kh.project.domain.seller.svc;

import com.kh.project.domain.common.ApiResponseCode;
import com.kh.project.domain.exception.BusinessException;
import com.kh.project.domain.exception.UserException;
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
    if (sellerDAO.existsByEmail(seller.getEmail())) {
      throw new BusinessException(ApiResponseCode.USER_ALREADY_EXISTS);
    }
    return sellerDAO.save(seller);
  }

  @Override
  @Transactional(readOnly = true)
  public Seller login(String email, String password) {
    return sellerDAO.findByEmail(email)
        .filter(seller -> seller.getPassword().trim().equals(password.trim()))
        .orElseThrow(() -> new UserException.LoginFailed("아이디 또는 비밀번호가 일치하지 않습니다."));
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
    return sellerDAO.existsByEmail(email);
  }

  @Override
  @Transactional(readOnly = true)
  public boolean checkPassword(Long sellerId, String password) {
    return sellerDAO.findById(sellerId)
        .map(seller -> seller.getPassword().trim().equals(password.trim()))
        .orElse(false);
  }
}