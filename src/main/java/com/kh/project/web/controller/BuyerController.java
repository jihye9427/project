package com.kh.project.web.controller;

import com.kh.project.domain.buyer.entity.Buyer;
import com.kh.project.domain.buyer.svc.BuyerSVC;
import com.kh.project.domain.common.ApiResponse;
import com.kh.project.domain.common.ApiResponseCode;
import com.kh.project.domain.common.LoginDTO;
import com.kh.project.domain.exception.UserException;
import com.kh.project.web.api.buyer.BuyerDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.net.URI;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/buyer")
public class BuyerController {
    private final BuyerSVC buyerSVC;
    public static final String LOGIN_BUYER = "loginBuyer";

    // 회원가입
    @PostMapping("/join")
    public String join(BuyerDTO.JoinReq joinReq){
        Buyer buyer = new Buyer();
        BeanUtils.copyProperties(joinReq, buyer);
        buyer.setGubun("MD101"); // 일반구매자
        buyerSVC.join(buyer);
        return "redirect:/login";
    }

    @PostMapping("/login")
    public String login(LoginDTO loginDTO, HttpServletRequest request, Model model) {
        try {
            Buyer loginBuyer = buyerSVC.login(loginDTO.getEmail(), loginDTO.getPassword());
            request.getSession(true).setAttribute(LOGIN_BUYER, loginBuyer);
            return "redirect:/";
        } catch (UserException.LoginFailed e) {
            model.addAttribute("errorMessage", "아이디 또는 비밀번호가 일치하지 않습니다.");
            return "buyer/buyer_login"; // 로그인 실패 시 다시 로그인 폼으로
        }
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/mypage")
    public String mypage(Model model, @SessionAttribute(name = LOGIN_BUYER, required = false) Buyer loginBuyer) {
        // 인터셉터가 이미 로그인 체크를 완료했으므로, 여기서는 바로 모델에 담아 사용합니다.
        model.addAttribute("buyer", loginBuyer);
        return "buyer/buyer_mypage";
    }

    // 회원정보 수정 페이지
    @GetMapping("/update")
    public String updateForm(Model model, @SessionAttribute(name = LOGIN_BUYER) Buyer loginBuyer) {
        model.addAttribute("buyer", loginBuyer);
        return "buyer/buyer_update";
    }

    // 회원정보 수정 처리
    @PostMapping("/update")
    public String updateInfo(BuyerDTO.UpdateReq updateReq, @SessionAttribute(name = LOGIN_BUYER) Buyer loginBuyer, HttpSession session, Model model) {
        // 비밀번호 확인
        if (!buyerSVC.checkPassword(loginBuyer.getBuyerId(), updateReq.getCurrentPassword())) {
            model.addAttribute("errorMessage", "현재 비밀번호가 일치하지 않습니다.");
            model.addAttribute("buyer", loginBuyer); // 사용자 정보를 다시 모델에 추가하여 폼을 보여줌
            return "buyer/buyer_update";
        }
        // 정보 수정
        Buyer buyerToUpdate = new Buyer();
        BeanUtils.copyProperties(updateReq, buyerToUpdate);
        buyerSVC.updateMemberInfo(loginBuyer.getBuyerId(), buyerToUpdate);

        // 세션 정보 업데이트
        buyerSVC.findMemberInfo(loginBuyer.getBuyerId()).ifPresent(updatedBuyer -> session.setAttribute(LOGIN_BUYER, updatedBuyer));

        return "redirect:/buyer/mypage";
    }

    // 회원탈퇴 페이지
    @GetMapping("/leave")
    public String leaveForm() {
        return "buyer/buyer_leave";
    }

    // 회원탈퇴 처리
    @PostMapping("/leave")
    public String leave(BuyerDTO.PasswordCheckReq leaveReq, @SessionAttribute(name = LOGIN_BUYER) Buyer loginBuyer, HttpSession session, Model model) {
        if (!buyerSVC.checkPassword(loginBuyer.getBuyerId(), leaveReq.getPassword())) {
            model.addAttribute("errorMessage", "비밀번호가 일치하지 않습니다.");
            return "buyer/buyer_leave";
        }

        buyerSVC.deleteMember(loginBuyer.getBuyerId());
        session.invalidate();
        return "redirect:/";
    }
} 