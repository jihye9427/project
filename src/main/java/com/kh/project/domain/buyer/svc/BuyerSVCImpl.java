package com.kh.project.domain.buyer.svc;

import com.kh.project.domain.buyer.dao.BuyerDAO;
import com.kh.project.domain.buyer.entity.Buyer;
import com.kh.project.domain.common.ApiResponseCode;
import com.kh.project.domain.exception.BusinessException;
import com.kh.project.domain.exception.UserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BuyerSVCImpl implements BuyerSVC {

    private final BuyerDAO buyerDAO;

    @Override
    public Buyer join(Buyer buyer) {
        log.info("구매자 회원가입 시도: {}", buyer.getEmail());
        if (buyerDAO.existsByEmail(buyer.getEmail())) {
            log.warn("이미 존재하는 이메일: {}", buyer.getEmail());
            throw new BusinessException(ApiResponseCode.USER_ALREADY_EXISTS);
        }

        Buyer savedBuyer = buyerDAO.save(buyer);
        log.info("구매자 회원가입 성공: {}", savedBuyer.getBuyerId());
        return savedBuyer;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Buyer> login(String email, String password) {
        log.info("구매자 로그인 시도: {}", email);

        if (email == null || password == null || email.trim().isEmpty() || password.trim().isEmpty()) {
            log.warn("이메일 또는 비밀번호가 비어있습니다.");
            return Optional.empty(); // 실패 시 빈 Optional 반환
        }

        String cleanEmail = email.trim();
        String cleanPassword = password.trim();

        // findByEmail의 결과가 Optional이므로, 함수형 스타일로 처리
        return buyerDAO.findByEmail(cleanEmail)
            .map(buyer -> {
                // 비밀번호 확인
                if (!buyer.getPassword().equals(cleanPassword)) {
                    log.warn("비밀번호 불일치: {}", cleanEmail);
                    return null; // filter에서 걸러짐
                }
                // 계정 상태 확인
                if (!buyer.canLogin()) {
                    // 구체적인 상태별 로깅
                    if (buyer.isWithdrawn()) {
                        log.warn("탈퇴한 회원 로그인 시도: {}", cleanEmail);
                    } else if (buyer.isSuspended()) {
                        log.warn("정지된 계정 로그인 시도: {}", cleanEmail);
                    } else if (buyer.isDormant()) {
                        log.warn("휴면 계정 로그인 시도: {}", cleanEmail);
                    } else {
                        log.warn("비활성 계정 로그인 시도: {} (상태: {})", cleanEmail, buyer.getStatus());
                    }
                    return null; // filter에서 걸러짐
                }
                log.info("구매자 로그인 성공: {}", buyer.getBuyerId());
                return buyer;
            })
            .filter(buyer -> buyer != null);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Buyer> findMemberInfo(Long buyerId) {
        return buyerDAO.findById(buyerId);
    }

    @Override
    public int updateMemberInfo(Long buyerId, Buyer buyer) {
        return buyerDAO.update(buyerId, buyer);
    }

    @Override
    public int deleteMember(Long buyerId) {
        return buyerDAO.delete(buyerId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isExistEmail(String email) {
        return buyerDAO.existsByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkPassword(Long buyerId, String password) {
        Optional<Buyer> optionalBuyer = buyerDAO.findById(buyerId);
        return optionalBuyer.map(buyer -> buyer.getPassword().trim().equals(password.trim()))
                .orElse(false);
    }
}