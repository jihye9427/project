package com.kh.project.domain.buyer.svc;

import com.kh.project.domain.buyer.dao.BuyerDAO;
import com.kh.project.domain.buyer.entity.Buyer;
import com.kh.project.domain.exception.BusinessException;
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
        if (buyerDAO.isExistEmail(buyer.getEmail())) {
            throw new BusinessException("이미 사용중인 이메일입니다.");
        }
        return buyerDAO.save(buyer);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Buyer> login(String email, String password) {
        Optional<Buyer> optionalBuyer = buyerDAO.findByEmail(email);

        if (optionalBuyer.isEmpty() || !optionalBuyer.get().getPassword().equals(password)) {
            return Optional.empty();
        }
        return optionalBuyer;
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
        return buyerDAO.isExistEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkPassword(Long buyerId, String password) {
        Optional<Buyer> optionalBuyer = buyerDAO.findById(buyerId);
        return optionalBuyer.map(buyer -> buyer.getPassword().equals(password))
                .orElse(false);
    }
}