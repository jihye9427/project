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

    // 입력값 검증
    if (email == null || password == null || email.trim().isEmpty() || password.trim().isEmpty()) {
      log.warn("이메일 또는 비밀번호가 비어있습니다.");
      return Optional.empty();
    }

    String cleanEmail = email.trim();
    String cleanPassword = password.trim();

    // findByEmail의 결과가 Optional이므로, 함수형 스타일로 처리
    return sellerDAO.findByEmail(cleanEmail)
        .map(seller -> {
          // 비밀번호 확인
          if (!seller.getPassword().equals(cleanPassword)) {
            log.warn("비밀번호 불일치: {}", cleanEmail);
            return null; // filter에서 걸러짐
          }

          // 계정 상태 확인
          if (!seller.canLogin()) {
            // 구체적인 상태별 로깅
            if (seller.isWithdrawn()) {
              log.warn("탈퇴한 판매자 로그인 시도: {}", cleanEmail);
            } else if (seller.isSuspended()) {
              log.warn("정지된 판매자 계정 로그인 시도: {}", cleanEmail);
            } else if (seller.isDormant()) {
              log.warn("휴면 판매자 계정 로그인 시도: {}", cleanEmail);
            } else {
              log.warn("비활성 판매자 계정 로그인 시도: {} (상태: {})", cleanEmail, seller.getStatus());
            }
            return null; // filter에서 걸러짐
          }

          log.info("판매자 로그인 성공: {}", seller.getSellerId());
          return seller;
        })
        .filter(seller -> seller != null); // null인 경우 Optional.empty() 반환
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<Seller> findMemberInfo(Long sellerId) {
    log.debug("판매자 정보 조회: {}", sellerId);
    return sellerDAO.findById(sellerId);
  }

  @Override
  public int updateMemberInfo(Long sellerId, Seller seller) {
    log.info("판매자 정보 수정: {}", sellerId);
    int result = sellerDAO.update(sellerId, seller);

    if (result > 0) {
      log.info("판매자 정보 수정 성공: {}", sellerId);
    } else {
      log.warn("판매자 정보 수정 실패: {}", sellerId);
    }

    return result;
  }

  @Override
  public int deleteMember(Long sellerId) {
    log.info("판매자 탈퇴: {}", sellerId);
    int result = sellerDAO.delete(sellerId);

    if (result > 0) {
      log.info("판매자 탈퇴 성공: {}", sellerId);
    } else {
      log.warn("판매자 탈퇴 실패: {}", sellerId);
    }

    return result;
  }

  @Override
  @Transactional(readOnly = true)
  public boolean isExistEmail(String email) {
    log.debug("판매자 이메일 중복 체크: {}", email);
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