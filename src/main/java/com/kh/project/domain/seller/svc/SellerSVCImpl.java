package com.kh.project.domain.seller.svc;

import com.kh.project.domain.common.ApiResponseCode;
import com.kh.project.domain.exception.BusinessException;
import com.kh.project.domain.exception.UserException;
import com.kh.project.domain.seller.dao.SellerDAO;
import com.kh.project.domain.seller.entity.Seller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class SellerSVCImpl implements SellerSVC {

  private final SellerDAO sellerDAO;

  @Override
  public Seller join(Seller seller) {
    log.info("판매자 회원가입 시도: {}", seller.getEmail());

    if (sellerDAO.existsByEmail(seller.getEmail())) {
      log.warn("이미 존재하는 이메일: {}", seller.getEmail());
      throw new BusinessException(ApiResponseCode.USER_ALREADY_EXISTS);
    }

    Seller savedSeller = sellerDAO.save(seller);
    log.info("판매자 회원가입 성공: {}", savedSeller.getSellerId());
    return savedSeller;
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<Seller> login(String email, String password) {
    log.info("판매자 로그인 시도: {}", email);

    if (email == null || password == null || email.trim().isEmpty() || password.trim().isEmpty()) {
      log.warn("이메일 또는 비밀번호가 비어있습니다.");
      return Optional.empty();
    }

    String cleanEmail = email.trim();
    String cleanPassword = password.trim();

    return sellerDAO.findByEmail(cleanEmail)
        .filter(seller -> {
            if (!seller.getPassword().equals(cleanPassword)) {
                log.warn("비밀번호 불일치: {}", cleanEmail);
                return false;
            }
            // 엔티티에 canLogin() 같은 상태 확인 메소드가 있다고 가정
            // if (!seller.canLogin()) { ... }
            log.info("판매자 로그인 성공: {}", seller.getSellerId());
            return true;
        });
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
    if (password == null) {
      return false;
    }
    Optional<Seller> optionalSeller = sellerDAO.findById(sellerId);
    return optionalSeller
        .map(seller -> seller.getPassword().trim().equals(password.trim()))
        .orElse(false);
  }
}