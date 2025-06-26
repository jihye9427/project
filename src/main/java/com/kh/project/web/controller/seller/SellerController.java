package com.kh.project.web.controller.seller;

import com.kh.project.domain.common.LoginDTO;
import com.kh.project.domain.seller.entity.Seller;
import com.kh.project.domain.seller.svc.SellerSVC;
import com.kh.project.web.api.seller.SellerDTO.*;
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
@RequestMapping("/seller")
public class SellerController {
    private final SellerSVC sellerSVC;
    public static final String LOGIN_SELLER = "loginSeller";

    // ===============================
    // 페이지 보여주기 (GET 메소들)
    // ===============================

    /**
     * 로그인 페이지 보여주기
     */
    @GetMapping("/login")
    public String loginForm() {
        return "seller/seller_login";
    }

    /**
     * 회원가입 페이지 보여주기
     */
    @GetMapping("/signup")
    public String signupForm() {
        return "seller/seller_signup";
    }

    /**
     * 회원탈퇴 페이지 보여주기
     */
    @GetMapping("/leave")
    public String leaveForm(@SessionAttribute(name = LOGIN_SELLER) Seller loginSeller) {
        // 로그인 체크는 인터셉터에서 처리하므로, 이 핸들러는 로그인된 사용자만 접근 가능
        return "seller/seller_leave";
    }

    // ===============================
    // 비즈니스 로직 처리 (POST 메소드들)
    // ===============================

    /**
     * 회원가입 처리
     */
    @PostMapping("/signup")
    public String signup(@Valid @ModelAttribute JoinRequest joinReq) {
        Seller seller = new Seller();
        BeanUtils.copyProperties(joinReq, seller);
        sellerSVC.join(seller);
        return "redirect:/";
    }

    /**
     * 로그인 처리
     */
    @PostMapping("/login")
    public String login(@Valid @ModelAttribute LoginDTO loginDTO, HttpSession session) {
        Seller loginSeller = sellerSVC.login(loginDTO.getEmail(), loginDTO.getPassword());
        session.setAttribute(LOGIN_SELLER, loginSeller);
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
    public String mypage(HttpSession session, Model model) {
        Seller loginSeller = (Seller) session.getAttribute(LOGIN_SELLER);
        if (loginSeller == null) {
            return "redirect:/login";
        }
        
        Optional<Seller> memberInfo = sellerSVC.findMemberInfo(loginSeller.getSellerId());
        model.addAttribute("seller", memberInfo.orElseThrow());
        return "seller/seller_mypage";
    }

    /**
     * 회원정보 수정 처리
     */
    @PostMapping("/mypage")
    public String updateInfo(@Valid @ModelAttribute UpdateRequest updateReq,
                           @SessionAttribute(name = LOGIN_SELLER) Seller loginSeller,
                           HttpSession session) {
        if (!sellerSVC.checkPassword(loginSeller.getSellerId(), updateReq.getCurrentPassword())) {
            return "redirect:/seller/mypage?error=password";
        }
        Seller sellerToUpdate = new Seller();
        BeanUtils.copyProperties(updateReq, sellerToUpdate);
        sellerSVC.updateMemberInfo(loginSeller.getSellerId(), sellerToUpdate);

        sellerSVC.findMemberInfo(loginSeller.getSellerId())
                .ifPresent(updatedSeller -> session.setAttribute(LOGIN_SELLER, updatedSeller));

        return "redirect:/seller/mypage";
    }

    /**
     * 회원탈퇴 처리
     */
    @PostMapping("/leave")
    public String leave(@ModelAttribute PasswordCheckRequest leaveReq,
                       @SessionAttribute(name = LOGIN_SELLER) Seller loginSeller,
                       HttpSession session) {
        if (!sellerSVC.checkPassword(loginSeller.getSellerId(), leaveReq.getPassword())) {
            return "redirect:/seller/leave?error=password";
        }

        sellerSVC.deleteMember(loginSeller.getSellerId());
        session.invalidate();
        return "redirect:/";
    }
}
