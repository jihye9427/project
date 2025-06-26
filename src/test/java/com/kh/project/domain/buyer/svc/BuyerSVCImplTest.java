package com.kh.project.domain.buyer.svc;

import com.kh.project.domain.buyer.dao.BuyerDAO;
import com.kh.project.domain.buyer.entity.Buyer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Optional;
import static com.kh.project.domain.exception.UserException.EmailAlreadyExists;
import static com.kh.project.domain.exception.UserException.LoginFailed;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@ExtendWith(MockitoExtension.class)
class BuyerSVCImplTest {

  @InjectMocks
  private BuyerSVCImpl buyerSVC;

  @Mock
  private BuyerDAO buyerDAO;

  private Buyer createSampleBuyer() {
    Buyer buyer = new Buyer();
    buyer.setBuyerId(1L);
    buyer.setEmail("buyer@test.com");
    buyer.setPassword("password123");
    buyer.setNickname("testnick");
    buyer.setName("Test Buyer");
    buyer.setTel("010-0000-0000");
    return buyer;
  }

  @Test
  @DisplayName("회원 정보 조회 - 성공")
  void findMemberInfo_success() {
    // given: DAO가 ID=1로 조회하면, 샘플 구매자 정보를 반환한다고 가정
    Buyer sampleBuyer = createSampleBuyer();
    when(buyerDAO.findById(1L)).thenReturn(Optional.of(sampleBuyer));

    // when: 서비스의 정보 조회 메소드 실행
    Optional<Buyer> result = buyerSVC.findMemberInfo(1L);

    // then: 결과가 존재하고, 이메일이 일치하는지 검증
    assertTrue(result.isPresent());
    assertEquals("buyer@test.com", result.get().getEmail());
  }

  @Test
  @DisplayName("회원 정보 조회 - 실패 (사용자 없음)")
  void findMemberInfo_fail() {
    // given: DAO가 어떤 ID로 조회하든, 결과가 없다고(empty) 가정
    when(buyerDAO.findById(anyLong())).thenReturn(Optional.empty());

    // when: 서비스의 정보 조회 메소드 실행
    Optional<Buyer> result = buyerSVC.findMemberInfo(99L); // 없는 ID로 조회 시도

    // then: 결과가 비어있는지 검증
    assertTrue(result.isEmpty());
  }

  @Test
  @DisplayName("회원가입 - 성공")
  void join_success() {
    // given: 이메일과 닉네임이 중복되지 않는다고 가정
    Buyer newBuyer = createSampleBuyer();
    when(buyerDAO.existsByEmail(newBuyer.getEmail())).thenReturn(false);
    when(buyerDAO.existsByNickname(newBuyer.getNickname())).thenReturn(false);
    when(buyerDAO.save(any(Buyer.class))).thenReturn(newBuyer); // 저장 성공 시 객체 반환

    // when: 회원가입 실행
    Buyer joinedBuyer = buyerSVC.join(newBuyer);

    // then: 반환된 객체가 null이 아니고, 이메일이 일치하는지 검증
    assertNotNull(joinedBuyer);
    assertEquals("buyer@test.com", joinedBuyer.getEmail());
  }

  @Test
  @DisplayName("회원가입 - 실패 (이메일 중복)")
  void join_fail_email_exists() {
    // given: 이메일이 이미 존재한다고 가정
    Buyer newBuyer = createSampleBuyer();
    when(buyerDAO.existsByEmail(newBuyer.getEmail())).thenReturn(true);

    // when & then: EmailAlreadyExists 예외가 발생하는지 검증
    assertThrows(EmailAlreadyExists.class, () -> {
      buyerSVC.join(newBuyer);
    });
  }

  @Test
  @DisplayName("로그인 - 성공")
  void login_success() {
    // given: DAO가 이메일로 조회하면, 정확한 비밀번호를 가진 사용자를 반환한다고 가정
    Buyer storedBuyer = createSampleBuyer();
    when(buyerDAO.findByEmail("buyer@test.com")).thenReturn(Optional.of(storedBuyer));

    // when: 올바른 정보로 로그인 실행
    Buyer loginUser = buyerSVC.login("buyer@test.com", "password123");

    // then: 반환된 객체가 null이 아니고, ID가 일치하는지 검증
    assertNotNull(loginUser);
    assertEquals(1L, loginUser.getBuyerId());
  }

  @Test
  @DisplayName("로그인 - 실패 (비밀번호 불일치)")
  void login_fail_password_mismatched() {
    // given: DAO가 이메일로 조회하면, 다른 비밀번호를 가진 사용자를 반환한다고 가정
    Buyer storedBuyer = createSampleBuyer();
    when(buyerDAO.findByEmail("buyer@test.com")).thenReturn(Optional.of(storedBuyer));

    // when & then: 틀린 비밀번호로 로그인 시 LoginFailed 예외가 발생하는지 검증
    assertThrows(LoginFailed.class, () -> {
      buyerSVC.login("buyer@test.com", "wrong_password");
    });
  }
}