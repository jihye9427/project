package com.kh.project.web.controller;

import com.kh.project.domain.common.LoginDTO;
import com.kh.project.domain.exception.UserException;
import com.kh.project.domain.seller.entity.Seller;
import com.kh.project.domain.seller.svc.SellerSVC;
import com.kh.project.web.api.seller.SellerDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/seller")
public class SellerController {
    private final SellerSVC sellerSVC;
    public static final String LOGIN_SELLER = "loginSeller";

    // 회원가입
    @PostMapping("/join")
    public String join(SellerDTO.JoinReq joinReq){
        Seller seller = new Seller();
        BeanUtils.copyProperties(joinReq, seller);
        // seller.setGubun("MD102"); // 'gubun' 필드가 Seller 엔티티에 존재하지 않으므로 주석 처리 또는 삭제
        sellerSVC.join(seller);
        return "redirect:/login";
    }

    @PostMapping("/login")
    public String login(LoginDTO loginDTO, HttpServletRequest request, Model model) {
        try {
            Seller loginSeller = sellerSVC.login(loginDTO.getEmail(), loginDTO.getPassword());
            request.getSession(true).setAttribute(LOGIN_SELLER, loginSeller);
            return "redirect:/";
        } catch (UserException.LoginFailed e) {
            model.addAttribute("errorMessage", "아이디 또는 비밀번호가 일치하지 않습니다.");
            return "seller/seller_login"; // 로그인 실패 시 다시 로그인 폼으로
        }
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/mypage")
    public String mypage(Model model, @SessionAttribute(name = LOGIN_SELLER, required = false) Seller loginSeller) {
        model.addAttribute("seller", loginSeller);
        return "seller/seller_mypage";
    }

    @GetMapping("/update")
    public String updateForm(Model model, @SessionAttribute(name = LOGIN_SELLER) Seller loginSeller) {
        model.addAttribute("seller", loginSeller);
        return "seller/seller_update";
    }

    @PostMapping("/update")
    public String updateInfo(SellerDTO.UpdateReq updateReq, @SessionAttribute(name = LOGIN_SELLER) Seller loginSeller, HttpSession session, Model model) {
        if (!sellerSVC.checkPassword(loginSeller.getSellerId(), updateReq.getCurrentPassword())) {
            model.addAttribute("errorMessage", "현재 비밀번호가 일치하지 않습니다.");
            model.addAttribute("seller", loginSeller);
            return "seller/seller_update";
        }
        Seller sellerToUpdate = new Seller();
        BeanUtils.copyProperties(updateReq, sellerToUpdate);
        sellerSVC.updateMemberInfo(loginSeller.getSellerId(), sellerToUpdate);
        sellerSVC.findMemberInfo(loginSeller.getSellerId()).ifPresent(updatedSeller -> session.setAttribute(LOGIN_SELLER, updatedSeller));
        return "redirect:/seller/mypage";
    }

    @GetMapping("/leave")
    public String leaveForm() {
        return "seller/seller_leave";
    }

    @PostMapping("/leave")
    public String leave(SellerDTO.PasswordCheckReq leaveReq, @SessionAttribute(name = LOGIN_SELLER) Seller loginSeller, HttpSession session, Model model) {
        if (!sellerSVC.checkPassword(loginSeller.getSellerId(), leaveReq.getPassword())) {
            model.addAttribute("errorMessage", "비밀번호가 일치하지 않습니다.");
            return "seller/seller_leave";
        }

        sellerSVC.deleteMember(loginSeller.getSellerId());
        session.invalidate();
        return "redirect:/";
    }
} 