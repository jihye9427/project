package com.kh.project.web.api.buyer;

import com.kh.project.domain.buyer.entity.Buyer;
import com.kh.project.domain.buyer.svc.BuyerSVC;
import com.kh.project.domain.common.ApiResponse;
import com.kh.project.domain.common.LoginDTO;
import com.kh.project.web.api.buyer.BuyerDTO;
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
public class BuyerController {
    private final BuyerSVC buyerSVC;
    public static final String LOGIN_BUYER = "loginBuyer";

    /**
     * 회원가입 처리 API
     */
    @PostMapping("/join")
    public ApiResponse<Object> join(@Valid @RequestBody BuyerDTO.JoinRequest joinReq) {
        Buyer buyer = new Buyer();
        BeanUtils.copyProperties(joinReq, buyer);
        // gubun, status 등 기본값 설정 로직 추가 가능
        Buyer joinedBuyer = buyerSVC.join(buyer);

        // 프론트엔드에 전달할 데이터 구성 (예: 회원 ID)
        BuyerDTO.JoinResponse res = new BuyerDTO.JoinResponse(joinedBuyer.getBuyerId(), joinedBuyer.getEmail(), joinedBuyer.getName());
        return ApiResponse.create("0", "성공", res);
    }

    /**
     * 로그인 처리 API
     */
    @PostMapping("/login")
    public ApiResponse<Object> login(@Valid @RequestBody LoginDTO loginDTO, HttpServletRequest request) {
        // 1) 이메일, 비밀번호로 회원 검증
        Optional<Buyer> optionalBuyer = buyerSVC.login(loginDTO.getEmail(), loginDTO.getPassword());
        if (optionalBuyer.isEmpty()) {
            return ApiResponse.create("M0102", "회원 정보가 없습니다.", null);
        }

        // 2) 세션 생성 및 회원 정보 저장
        HttpSession session = request.getSession(true);
        session.setAttribute(LOGIN_BUYER, optionalBuyer.get());

        return ApiResponse.create("0", "성공", null);
    }

    /**
     * 로그아웃 처리 API
     */
    @PostMapping("/logout")
    public ApiResponse<Object> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return ApiResponse.create("0", "로그아웃되었습니다.", null);
    }

    /**
     * 현재 로그인한 회원 정보 조회
     */
    @GetMapping("/info")
    public ApiResponse<Object> getMemberInfo(HttpSession session) {
        Buyer loginBuyer = (Buyer) session.getAttribute(LOGIN_BUYER);
        if (loginBuyer == null) {
            return ApiResponse.create("M0103", "로그인 정보가 없습니다.", null);
        }
        
        Optional<Buyer> memberInfo = buyerSVC.findMemberInfo(loginBuyer.getBuyerId());
        return ApiResponse.create("0", "성공", memberInfo.orElse(null));
    }

    /**
     * 회원정보 수정
     */
    @PatchMapping("/info")
    public ApiResponse<Object> updateMemberInfo(
            @Valid @RequestBody BuyerDTO.UpdateRequest updateReq,
            HttpSession session
    ) {
        Buyer loginBuyer = (Buyer) session.getAttribute(LOGIN_BUYER);
        if (loginBuyer == null) {
            return ApiResponse.create("M0103", "로그인 정보가 없습니다.", null);
        }

        // 현재 비밀번호 확인
        if (!buyerSVC.checkPassword(loginBuyer.getBuyerId(), updateReq.getCurrentPassword())) {
            return ApiResponse.create("M0104", "비밀번호가 일치하지 않습니다.", null);
        }

        // 정보 수정
        Buyer buyerToUpdate = new Buyer();
        BeanUtils.copyProperties(updateReq, buyerToUpdate);
        buyerSVC.updateMemberInfo(loginBuyer.getBuyerId(), buyerToUpdate);

        // 세션 정보 갱신
        buyerSVC.findMemberInfo(loginBuyer.getBuyerId())
                .ifPresent(updatedBuyer -> session.setAttribute(LOGIN_BUYER, updatedBuyer));

        return ApiResponse.create("0", "회원 정보가 수정되었습니다.", null);
    }

    /**
     * 회원 탈퇴
     */
    @PostMapping("/leave")
    public ApiResponse<Object> leaveMember(
            @RequestBody BuyerDTO.PasswordCheckReq leaveReq,
            HttpSession session
    ) {
        Buyer loginBuyer = (Buyer) session.getAttribute(LOGIN_BUYER);
        if (loginBuyer == null) {
            return ApiResponse.create("M0103", "로그인 정보가 없습니다.", null);
        }

        if (!buyerSVC.checkPassword(loginBuyer.getBuyerId(), leaveReq.getPassword())) {
            return ApiResponse.create("M0104", "비밀번호가 일치하지 않습니다.", null);
        }

        buyerSVC.deleteMember(loginBuyer.getBuyerId());
        session.invalidate();

        return ApiResponse.create("0", "회원 탈퇴가 완료되었습니다.", null);
    }
} 