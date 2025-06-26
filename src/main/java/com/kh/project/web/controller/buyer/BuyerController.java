package com.kh.project.web.controller.buyer;

import com.kh.project.domain.buyer.entity.Buyer;
import com.kh.project.domain.buyer.svc.BuyerSVC;
import com.kh.project.domain.common.LoginDTO;
import com.kh.project.web.api.buyer.BuyerDTO.JoinRequest;
import com.kh.project.web.api.buyer.BuyerDTO.PasswordCheckReq;
import com.kh.project.web.api.buyer.BuyerDTO.UpdateRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/buyer")
public class BuyerController {
    private final BuyerSVC buyerSVC;
    public static final String LOGIN_BUYER = "loginBuyer";
    

    // ===============================
    // 페이지 보여주기 (GET 메소들)
    // ===============================

    /**
     * 로그인 페이지 보여주기
     */
    @GetMapping("/login")
    public String loginForm() {
        return "buyer/buyer_login";
    }

    /**
     * 회원가입 페이지 보여주기
     */
    @GetMapping("/signup")
    public String signupForm() {
        return "buyer/buyer_signup";
    }

    /**
     * 회원탈퇴 페이지 보여주기
     */
    @GetMapping("/leave")
    public String leaveForm(@SessionAttribute(name = LOGIN_BUYER) Buyer loginBuyer) {
        // 로그인 체크는 인터셉터에서 처리하므로, 이 핸들러는 로그인된 사용자만 접근 가능
        return "buyer/buyer_leave";
    }

    // ===============================
    // 비즈니스 로직 처리 (POST 메소들)
    // ===============================

    /**
     * 회원가입 처리
     */
    @PostMapping("/signup")
    public String signup(@Valid @ModelAttribute JoinRequest joinReq) {
        Buyer buyer = new Buyer();
        BeanUtils.copyProperties(joinReq, buyer);
        buyerSVC.join(buyer);
        return "redirect:/";
    }

    /**
     * 로그인 처리
     */
    @PostMapping("/login")
    public String login(@Valid @ModelAttribute LoginDTO loginDTO, HttpSession session) {
        Buyer loginBuyer = buyerSVC.login(loginDTO.getEmail(), loginDTO.getPassword());
        session.setAttribute(LOGIN_BUYER, loginBuyer);
        return "redirect:/"; // 로그인 성공 시 메인 페이지로
    }

    /**
     * 로그아웃 처리
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    /**
     * 마이페이지 보여주기 (데이터 필요)
     */
    @GetMapping("/mypage")
    public String mypage(@SessionAttribute(name = LOGIN_BUYER) Buyer loginBuyer, Model model) {
        // 인터셉터가 로그인 여부를 체크해주므로, @SessionAttribute로 바로 정보를 받아옴
        Optional<Buyer> memberInfo = buyerSVC.findMemberInfo(loginBuyer.getBuyerId());
        model.addAttribute("buyer", memberInfo.orElse(null));
        return "buyer/buyer_mypage";
    }

    /**
     * 회원정보 수정 처리
     */
    @PostMapping("/mypage")
    public String updateInfo(@Valid @ModelAttribute UpdateRequest updateReq,
                           @SessionAttribute(name = LOGIN_BUYER) Buyer loginBuyer,
                           HttpSession session) {
        // 비밀번호 확인
        if (!buyerSVC.checkPassword(loginBuyer.getBuyerId(), updateReq.getCurrentPassword())) {
            return "redirect:/buyer/mypage?error=password"; // 실패 시 쿼리 파라미터로 에러 전달
        }
        // 정보 수정
        Buyer buyerToUpdate = new Buyer();
        BeanUtils.copyProperties(updateReq, buyerToUpdate);
        buyerSVC.updateMemberInfo(loginBuyer.getBuyerId(), buyerToUpdate);

        // 세션 정보 갱신
        buyerSVC.findMemberInfo(loginBuyer.getBuyerId())
                .ifPresent(updatedBuyer -> session.setAttribute(LOGIN_BUYER, updatedBuyer));

        return "redirect:/buyer/mypage";
    }

    /**
     * 회원탈퇴 처리
     */
    @PostMapping("/leave")
    public String leave(@ModelAttribute PasswordCheckReq leaveReq,
                       @SessionAttribute(name = LOGIN_BUYER) Buyer loginBuyer,
                       HttpSession session) {
        if (!buyerSVC.checkPassword(loginBuyer.getBuyerId(), leaveReq.getPassword())) {
            return "redirect:/buyer/leave?error=password";
        }

        buyerSVC.deleteMember(loginBuyer.getBuyerId());
        session.invalidate();
        return "redirect:/";
    }
} 