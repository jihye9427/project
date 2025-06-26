package com.kh.project.domain.buyer.svc;

import com.kh.project.domain.buyer.dao.BuyerDAO;
import com.kh.project.domain.buyer.entity.Buyer;
import com.kh.project.domain.common.ApiResponseCode;
import com.kh.project.domain.exception.BusinessException;
import com.kh.project.domain.exception.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class BuyerSVCImpl implements BuyerSVC {

    private final BuyerDAO buyerDAO;

    @Override
    public Buyer join(Buyer buyer) {
        if (buyerDAO.existsByEmail(buyer.getEmail())) {
            throw new BusinessException(ApiResponseCode.USER_ALREADY_EXISTS);
        }
        return buyerDAO.save(buyer);
    }

    @Override
    @Transactional(readOnly = true)
    public Buyer login(String email, String password) throws UserException.LoginFailed {
        return buyerDAO.findByEmail(email)
            .filter(buyer -> buyer.getPassword().equals(password))
            .orElseThrow(() -> new UserException.LoginFailed("아이디 또는 비밀번호가 일치하지 않습니다."));
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
        return optionalBuyer.map(buyer -> buyer.getPassword().equals(password))
                .orElse(false);
    }
}