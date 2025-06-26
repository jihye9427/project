package com.kh.project.domain.seller.svc;

import com.kh.project.domain.seller.dao.SellerDAO;
import com.kh.project.domain.seller.entity.Seller;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static com.kh.project.domain.exception.UserException.BizRegNoAlreadyExists;
import static com.kh.project.domain.exception.UserException.LoginFailed;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
@SpringBootTest
@ExtendWith(MockitoExtension.class)
class SellerSVCImplTest {

  @InjectMocks
  private SellerSVCImpl sellerSVC;
  @Mock
  private SellerDAO sellerDAO;

  private Seller createSampleSeller() {
    Seller seller = new Seller();
    seller.setSellerId(1L);
    seller.setEmail("seller@shop.com");
    seller.setPassword("shoppassword");
    seller.setBusinessNumber("111-22-33333");
    seller.setShopName("My Awesome Shop");
    seller.setSellerName("John Doe");
    seller.setShopAddress("123 Market St");
    seller.setTel("010-1234-5678");
    return seller;
  }

  @Test
  @DisplayName("판매자 회원가입 - 성공")
  void join_success() {
    // given: 이메일과 사업자 번호가 중복되지 않는다고 가정
    Seller newSeller = createSampleSeller();
    when(sellerDAO.existsByEmail(newSeller.getEmail())).thenReturn(false);
    when(sellerDAO.existsByBizRegNo(newSeller.getBusinessNumber())).thenReturn(false);
    when(sellerDAO.save(any(Seller.class))).thenReturn(newSeller);

    // when: 회원가입 실행
    Seller joinedSeller = sellerSVC.join(newSeller);

    // then: 결과 검증
    assertNotNull(joinedSeller);
    assertEquals("111-22-33333", joinedSeller.getBusinessNumber());
  }

  @Test
  @DisplayName("판매자 회원가입 - 실패 (사업자 번호 중복)")
  void join_fail_biz_reg_no_exists() {
    // given: 이메일은 중복이 아니지만, 사업자 번호는 중복이라고 가정
    Seller newSeller = createSampleSeller();
    when(sellerDAO.existsByEmail(newSeller.getEmail())).thenReturn(false);
    when(sellerDAO.existsByBizRegNo(newSeller.getBusinessNumber())).thenReturn(true);

    // when & then: BizRegNoAlreadyExists 예외가 발생하는지 검증
    assertThrows(BizRegNoAlreadyExists.class, () -> {
      sellerSVC.join(newSeller);
    });
  }

  @Test
  @DisplayName("판매자 로그인 - 실패 (사용자 없음)")
  void login_fail_user_not_found() {
    // given: DAO가 이메일로 조회하면, 결과가 없다고(empty) 가정
    when(sellerDAO.findByEmail("seller@shop.com")).thenReturn(Optional.empty());

    // when & then: LoginFailed 예외가 발생하는지 검증
    assertThrows(LoginFailed.class, () -> {
      sellerSVC.login("seller@shop.com", "shoppassword");
    });
  }
}