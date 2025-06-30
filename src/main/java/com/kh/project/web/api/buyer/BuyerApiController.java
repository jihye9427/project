package com.kh.project.web.api.buyer;

import com.kh.project.domain.buyer.entity.Buyer;
import com.kh.project.domain.buyer.svc.BuyerSVC;
import com.kh.project.domain.common.ApiResponse;
import com.kh.project.domain.common.LoginDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/buyers")
public class BuyerApiController {
    private final BuyerSVC buyerSVC;
    public static final String LOGIN_BUYER = "loginBuyer";

    /**
     * 회원가입 처리
     */
    @PostMapping("/join")
    public ApiResponse<Object> join(@Valid @RequestBody BuyerDTO.JoinRequest joinReq) {
        try {
            log.info("구매자 회원가입 시도: {}", joinReq.getEmail());

            Buyer buyer = new Buyer();
            BeanUtils.copyProperties(joinReq, buyer);
            buyer.setStatus("활성");  // 기본값 설정
            buyer.setGubun("M0101"); // 기본값 설정

            Buyer joinedBuyer = buyerSVC.join(buyer);

            // 프론트엔드에 전달할 데이터 구성
            BuyerDTO.JoinResponse res = new BuyerDTO.JoinResponse(
                joinedBuyer.getBuyerId(),
                joinedBuyer.getEmail(),
                joinedBuyer.getName()
            );

            log.info("구매자 회원가입 성공: {}", joinedBuyer.getBuyerId());
            return ApiResponse.create("0", "회원가입이 완료되었습니다.", res);

        } catch (Exception e) {
            log.error("구매자 회원가입 실패: {}", e.getMessage());
            return ApiResponse.create("E001", e.getMessage());
        }
    }


    /**
     * 로그인 처리 API
     */
    @PostMapping("/login")
    public ApiResponse<Object> login(@Valid @RequestBody LoginDTO loginDTO, HttpServletRequest request) {
        try {
            log.info("구매자 로그인 시도: {}", loginDTO.getEmail());

            // 이메일, 비밀번호로 회원 검증
            Optional<Buyer> optionalBuyer = buyerSVC.login(loginDTO.getEmail(), loginDTO.getPassword());
            if (optionalBuyer.isEmpty()) {
                log.warn("구매자 로그인 실패: {}", loginDTO.getEmail());
                return ApiResponse.create("M0102", "아이디 또는 비밀번호가 일치하지 않습니다.");
            }

            // 세션 생성 및 회원 정보 저장
            HttpSession session = request.getSession(true);
            session.setAttribute(LOGIN_BUYER, optionalBuyer.get());

            log.info("구매자 로그인 성공: {}", optionalBuyer.get().getBuyerId());
            return ApiResponse.create("0", "로그인이 완료되었습니다.");

        } catch (Exception e) {
            log.error("구매자 로그인 중 오류: {}", e.getMessage());
            return ApiResponse.create("E002", "로그인 처리 중 오류가 발생했습니다.");
        }
    }

    /**
     * 로그아웃 처리 API
     */
    @PostMapping("/logout")
    public ApiResponse<Object> logout(HttpServletRequest request) {
        try {
            log.info("구매자 로그아웃 요청");

            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
                log.info("구매자 로그아웃 완료");
            }
            return ApiResponse.create("0", "로그아웃되었습니다.");

        } catch (Exception e) {
            log.error("구매자 로그아웃 중 오류: {}", e.getMessage());
            return ApiResponse.create("E003", "로그아웃 처리 중 오류가 발생했습니다.");
        }
    }

    /**
     * 로그인 상태 확인 API
     */
    @GetMapping("/check-login")
    public ApiResponse<Object> checkLogin(HttpSession session) {
        Buyer loginBuyer = (Buyer) session.getAttribute(LOGIN_BUYER);
        if (loginBuyer == null) {
            return ApiResponse.create("M0103", "로그인되지 않은 상태입니다.");
        }

        BuyerDTO.InformationResponse info = new BuyerDTO.InformationResponse();
        BeanUtils.copyProperties(loginBuyer, info);
        return ApiResponse.create("0", "로그인 상태입니다.", info);
    }


    /**
     * 이메일 중복 체크 API (회원가입 시 실시간 체크용)
     */
    @GetMapping("/check-email")
    public ApiResponse<Object> checkEmail(@RequestParam String email) {
        try {
            log.debug("이메일 중복 체크: {}", email);

            boolean exists = buyerSVC.isExistEmail(email);
            if (exists) {
                return ApiResponse.create("M0106", "이미 사용 중인 이메일입니다.");
            }
            return ApiResponse.create("0", "사용 가능한 이메일입니다.");

        } catch (Exception e) {
            log.error("이메일 중복 체크 실패: {}", e.getMessage());
            return ApiResponse.create("E007", "이메일 중복 체크 중 오류가 발생했습니다.");
        }
    }

    /**
     * 현재 로그인한 회원 정보 조회 API
     */
    @GetMapping("/info")
    public ApiResponse<Object> getMemberInfo(HttpSession session) {
        try {
            Buyer loginBuyer = (Buyer) session.getAttribute(LOGIN_BUYER);
            if (loginBuyer == null) {
                return ApiResponse.create("M0103", "로그인 정보가 없습니다.");
            }

            Optional<Buyer> memberInfo = buyerSVC.findMemberInfo(loginBuyer.getBuyerId());
            if (memberInfo.isEmpty()) {
                return ApiResponse.create("M0104", "회원 정보를 찾을 수 없습니다.");
            }

            // DTO로 변환하여 반환
            BuyerDTO.InformationResponse info = new BuyerDTO.InformationResponse();
            BeanUtils.copyProperties(memberInfo.get(), info);

            return ApiResponse.create("0", "조회가 완료되었습니다.", info);

        } catch (Exception e) {
            log.error("구매자 정보 조회 실패: {}", e.getMessage());
            return ApiResponse.create("E004", "회원정보 조회 중 오류가 발생했습니다.");
        }
    }
        


    /**
     * 회원정보 수정
     */
    @PatchMapping("/info")
    public ApiResponse<Object> updateMemberInfo(
        @Valid @RequestBody BuyerDTO.UpdateRequest updateReq,
        HttpSession session
    ) {
        try {
            log.info("구매자 정보 수정 요청");

            Buyer loginBuyer = (Buyer) session.getAttribute(LOGIN_BUYER);
            if (loginBuyer == null) {
                return ApiResponse.create("M0103", "로그인 정보가 없습니다.");
            }

            // 현재 비밀번호 확인
            if (!buyerSVC.checkPassword(loginBuyer.getBuyerId(), updateReq.getCurrentPassword())) {
                log.warn("비밀번호 불일치로 정보 수정 실패: {}", loginBuyer.getBuyerId());
                return ApiResponse.create("M0104", "현재 비밀번호가 일치하지 않습니다.");
            }

            // 정보 수정
            Buyer buyerToUpdate = new Buyer();
            BeanUtils.copyProperties(updateReq, buyerToUpdate);

            // 새 비밀번호가 있으면 설정
            if (updateReq.getNewPassword() != null && !updateReq.getNewPassword().trim().isEmpty()) {
                buyerToUpdate.setPassword(updateReq.getNewPassword());
            }

            buyerSVC.updateMemberInfo(loginBuyer.getBuyerId(), buyerToUpdate);

            // 세션 정보 갱신
            buyerSVC.findMemberInfo(loginBuyer.getBuyerId())
                .ifPresent(updatedBuyer -> session.setAttribute(LOGIN_BUYER, updatedBuyer));

            log.info("구매자 정보 수정 성공: {}", loginBuyer.getBuyerId());
            return ApiResponse.create("0", "회원 정보가 수정되었습니다.");

        } catch (Exception e) {
            log.error("구매자 정보 수정 실패: {}", e.getMessage());
            return ApiResponse.create("E005", "회원정보 수정 중 오류가 발생했습니다.");
        }
    }

    /**
     * 회원 탈퇴
     */
    @PostMapping("/leave")
    public ApiResponse<Object> leaveMember(
        @RequestBody BuyerDTO.PasswordCheckReq leaveReq,
        HttpSession session
    ) {
        try {
            log.info("구매자 탈퇴 요청");

            Buyer loginBuyer = (Buyer) session.getAttribute(LOGIN_BUYER);
            if (loginBuyer == null) {
                return ApiResponse.create("M0103", "로그인 정보가 없습니다.");
            }

            if (!buyerSVC.checkPassword(loginBuyer.getBuyerId(), leaveReq.getPassword())) {
                log.warn("비밀번호 불일치로 탈퇴 실패: {}", loginBuyer.getBuyerId());
                return ApiResponse.create("M0104", "비밀번호가 일치하지 않습니다.");
            }

            buyerSVC.deleteMember(loginBuyer.getBuyerId());
            session.invalidate();

            log.info("구매자 탈퇴 완료: {}", loginBuyer.getBuyerId());
            return ApiResponse.create("0", "회원 탈퇴가 완료되었습니다.");

        } catch (Exception e) {
            log.error("구매자 탈퇴 실패: {}", e.getMessage());
            return ApiResponse.create("E006", "회원탈퇴 처리 중 오류가 발생했습니다.");
        }
    }
}