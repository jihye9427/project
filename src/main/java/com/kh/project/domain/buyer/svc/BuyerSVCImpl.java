package com.kh.project.domain.buyer.svc;

import com.kh.project.domain.buyer.dao.BuyerDAO;
import com.kh.project.domain.buyer.entity.Buyer;
import com.kh.project.domain.common.ApiResponseCode;
import com.kh.project.domain.exception.BusinessException;
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
            return Optional.empty();
        }

        String cleanEmail = email.trim();
        String cleanPassword = password.trim();

        // findByEmail 메소드 사용 후 비밀번호 및 상태 체크
        return buyerDAO.findByEmail(cleanEmail)
            .filter(buyer -> {
                if (!buyer.getPassword().equals(cleanPassword)) {
                    log.warn("비밀번호 불일치: {}", cleanEmail);
                    return false;
                }
                // 엔티티에 canLogin() 같은 상태 확인 메소드가 있다고 가정
                // if (!buyer.canLogin()) {
                //    log.warn("로그인 불가 상태: {}", cleanEmail);
                //    return false;
                // }
                log.info("구매자 로그인 성공: {}", buyer.getBuyerId());
                return true;
            });
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