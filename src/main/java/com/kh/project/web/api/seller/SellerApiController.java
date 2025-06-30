package com.kh.project.web.api.seller;

import com.kh.project.domain.common.ApiResponse;
import com.kh.project.domain.common.LoginDTO;
import com.kh.project.domain.seller.entity.Seller;
import com.kh.project.domain.seller.svc.SellerSVC;
import com.kh.project.web.api.seller.SellerDTO.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sellers")
public class SellerApiController {
    private final SellerSVC sellerSVC;
    public static final String LOGIN_SELLER = "loginSeller";

    /**
     * 회원가입 처리 API
     */
    @PostMapping("/join")
    public ApiResponse<Object> join(@Valid @RequestBody SellerDTO.JoinRequest joinReq) {
        try {
            log.info("판매자 회원가입 시도: {}", joinReq.getEmail());

            Seller seller = new Seller();
            BeanUtils.copyProperties(joinReq, seller);
            seller.setStatus("활성");

            Seller joinedSeller = sellerSVC.join(seller);

            SellerDTO.InformationResponse res = new SellerDTO.InformationResponse();
            BeanUtils.copyProperties(joinedSeller, res);

            log.info("판매자 회원가입 성공: {}", joinedSeller.getSellerId());
            return ApiResponse.create("0", "회원가입이 완료되었습니다.", res);

        } catch (Exception e) {
            log.error("판매자 회원가입 실패: {}", e.getMessage());
            return ApiResponse.create("E001", e.getMessage());
        }
    }

    /**
     * 로그인 처리 API
     */
    @PostMapping("/login")
    public ApiResponse<Object> login(@Valid @RequestBody LoginDTO loginDTO, HttpServletRequest request) {
        try {
            log.info("판매자 로그인 시도: {}", loginDTO.getEmail());

            Optional<Seller> optionalSeller = sellerSVC.login(loginDTO.getEmail(), loginDTO.getPassword());
            if (optionalSeller.isEmpty()) {
                log.warn("판매자 로그인 실패: {}", loginDTO.getEmail());
                return ApiResponse.create("S0102", "아이디 또는 비밀번호가 일치하지 않습니다.");
            }

            HttpSession session = request.getSession(true);
            session.setAttribute(LOGIN_SELLER, optionalSeller.get());

            log.info("판매자 로그인 성공: {}", optionalSeller.get().getSellerId());
            return ApiResponse.create("0", "로그인이 완료되었습니다.");

        } catch (Exception e) {
            log.error("판매자 로그인 중 오류: {}", e.getMessage());
            return ApiResponse.create("E002", "로그인 처리 중 오류가 발생했습니다.");
        }
    }

    /**
     * 로그아웃 처리 API
     */
    @PostMapping("/logout")
    public ApiResponse<Object> logout(HttpServletRequest request) {
        try {
            log.info("판매자 로그아웃 요청");

            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
                log.info("판매자 로그아웃 완료");
            }
            return ApiResponse.create("0", "로그아웃되었습니다.");

        } catch (Exception e) {
            log.error("판매자 로그아웃 중 오류: {}", e.getMessage());
            return ApiResponse.create("E003", "로그아웃 처리 중 오류가 발생했습니다.");
        }
    }

    /**
     * 로그인 상태 확인 API
     */
    @GetMapping("/check-login")
    public ApiResponse<Object> checkLogin(HttpSession session) {
        Seller loginSeller = (Seller) session.getAttribute(LOGIN_SELLER);
        if (loginSeller == null) {
            return ApiResponse.create("S0103", "로그인되지 않은 상태입니다.");
        }

        SellerDTO.InformationResponse res = new SellerDTO.InformationResponse();
        BeanUtils.copyProperties(loginSeller, res);
        return ApiResponse.create("0", "로그인 상태입니다.", res);
    }

    /**
     * 이메일 중복 체크 API
     */
    @GetMapping("/check-email")
    public ApiResponse<Object> checkEmail(@RequestParam String email) {
        try {
            log.debug("이메일 중복 체크: {}", email);

            boolean exists = sellerSVC.isExistEmail(email);
            if (exists) {
                return ApiResponse.create("S0106", "이미 사용 중인 이메일입니다.");
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
            Seller loginSeller = (Seller) session.getAttribute(LOGIN_SELLER);
            if (loginSeller == null) {
                return ApiResponse.create("S0103", "로그인 정보가 없습니다.");
            }

            Optional<Seller> memberInfo = sellerSVC.findMemberInfo(loginSeller.getSellerId());
            if (memberInfo.isEmpty()) {
                return ApiResponse.create("S0104", "회원 정보를 찾을 수 없습니다.");
            }

            SellerDTO.InformationResponse res = new SellerDTO.InformationResponse();
            BeanUtils.copyProperties(memberInfo.get(), res);

            return ApiResponse.create("0", "조회가 완료되었습니다.", res);

        } catch (Exception e) {
            log.error("판매자 정보 조회 실패: {}", e.getMessage());
            return ApiResponse.create("E004", "회원정보 조회 중 오류가 발생했습니다.");
        }
    }

    /**
     * 회원정보 수정 API
     */
    @PatchMapping("/info")
    public ApiResponse<Object> updateMemberInfo(
        @Valid @RequestBody SellerDTO.UpdateRequest updateReq,
        HttpSession session
    ) {
        try {
            log.info("판매자 정보 수정 요청");

            Seller loginSeller = (Seller) session.getAttribute(LOGIN_SELLER);
            if (loginSeller == null) {
                return ApiResponse.create("S0103", "로그인 정보가 없습니다.");
            }

            if (!sellerSVC.checkPassword(loginSeller.getSellerId(), updateReq.getCurrentPassword())) {
                log.warn("비밀번호 불일치로 정보 수정 실패: {}", loginSeller.getSellerId());
                return ApiResponse.create("S0104", "현재 비밀번호가 일치하지 않습니다.");
            }

            Seller sellerToUpdate = new Seller();
            BeanUtils.copyProperties(updateReq, sellerToUpdate);

            // 새 비밀번호가 있으면 설정
            if (updateReq.getNewPassword() != null && !updateReq.getNewPassword().trim().isEmpty()) {
                sellerToUpdate.setPassword(updateReq.getNewPassword());
            }

            sellerSVC.updateMemberInfo(loginSeller.getSellerId(), sellerToUpdate);

            sellerSVC.findMemberInfo(loginSeller.getSellerId())
                .ifPresent(updatedSeller -> session.setAttribute(LOGIN_SELLER, updatedSeller));

            log.info("판매자 정보 수정 성공: {}", loginSeller.getSellerId());
            return ApiResponse.create("0", "회원 정보가 수정되었습니다.");

        } catch (Exception e) {
            log.error("판매자 정보 수정 실패: {}", e.getMessage());
            return ApiResponse.create("E005", "회원정보 수정 중 오류가 발생했습니다.");
        }
    }

    /**
     * 회원 탈퇴 API
     */
    @PostMapping("/leave")
    public ApiResponse<Object> leaveMember(
        @RequestBody SellerDTO.PasswordCheckRequest leaveReq,
        HttpSession session
    ) {
        try {
            log.info("판매자 탈퇴 요청");

            Seller loginSeller = (Seller) session.getAttribute(LOGIN_SELLER);
            if (loginSeller == null) {
                return ApiResponse.create("S0103", "로그인 정보가 없습니다.");
            }

            if (!sellerSVC.checkPassword(loginSeller.getSellerId(), leaveReq.getPassword())) {
                log.warn("비밀번호 불일치로 탈퇴 실패: {}", loginSeller.getSellerId());
                return ApiResponse.create("S0104", "비밀번호가 일치하지 않습니다.");
            }

            sellerSVC.deleteMember(loginSeller.getSellerId());
            session.invalidate();

            log.info("판매자 탈퇴 완료: {}", loginSeller.getSellerId());
            return ApiResponse.create("0", "회원 탈퇴가 완료되었습니다.");

        } catch (Exception e) {
            log.error("판매자 탈퇴 실패: {}", e.getMessage());
            return ApiResponse.create("E006", "회원탈퇴 처리 중 오류가 발생했습니다.");
        }
    }
}