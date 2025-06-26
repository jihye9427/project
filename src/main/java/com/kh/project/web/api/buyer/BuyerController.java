package com.kh.project.web.api.buyer;

import com.kh.project.domain.buyer.entity.Buyer;
import com.kh.project.domain.buyer.svc.BuyerSVC;
import com.kh.project.domain.common.ApiResponse;
import com.kh.project.domain.common.ApiResponseCode;
import com.kh.project.domain.common.LoginDTO;
import com.kh.project.domain.exception.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URI;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/buyer")
public class BuyerController {
  private final BuyerSVC buyerSVC;
  public static final String LOGIN_BUYER = "loginBuyer";

  // --- 페이지 로딩 메소드 ---
  @GetMapping("/login") public String loginForm() { return "buyer/buyer_login"; }
  @GetMapping("/signup") public String signupForm() { return "buyer/buyer_signup"; }
  @GetMapping("/mypage") public String mypage() { return "buyer/buyer_mypage"; }
  @GetMapping("/update") public String updateForm() { return "buyer/buyer_update"; }
  @GetMapping("/leave") public String leaveForm() { return "buyer/buyer_leave"; }
  @GetMapping({"/", "/index"}) public String index() { return "buyer/buyer_index"; }


  // --- 데이터 처리 메소드 (콘텐츠 협상) ---

  @PostMapping("/join")
  public ResponseEntity<?> join(
      @Valid @RequestBody(required = false) BuyerDTO.JoinReq jsonReq,
      @ModelAttribute BuyerDTO.JoinReq formReq,
      HttpServletRequest request) {

    BuyerDTO.JoinReq joinReq = (jsonReq != null) ? jsonReq : formReq;

    Buyer buyer = new Buyer();
    BeanUtils.copyProperties(joinReq, buyer);
    buyer.setGubun("MD101");

    try {
      Buyer joinedBuyer = buyerSVC.join(buyer);
      if (isAjaxRequest(request)) {
        return ResponseEntity.ok(ApiResponse.of(ApiResponseCode.SUCCESS, joinedBuyer.getBuyerId()));
      } else {
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("/buyer/login")).build();
      }
    } catch (BusinessException e) {
      if (isAjaxRequest(request)) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.of(e.getResponseCode(), null));
      } else {
        // Redirect with error (implementation depends on how you handle errors in views)
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("/buyer/signup?error")).build();
      }
    }
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(
      @RequestBody(required = false) LoginDTO jsonLogin,
      @ModelAttribute LoginDTO formLogin,
      HttpServletRequest request) {

    LoginDTO loginDTO = (jsonLogin != null) ? jsonLogin : formLogin;

    try {
      Buyer loginBuyer = buyerSVC.login(loginDTO.getEmail(), loginDTO.getPassword());
      request.getSession(true).setAttribute(LOGIN_BUYER, loginBuyer);

      if (isAjaxRequest(request)) {
        BuyerDTO.InfoRes res = new BuyerDTO.InfoRes();
        BeanUtils.copyProperties(loginBuyer, res);
        return ResponseEntity.ok(ApiResponse.of(ApiResponseCode.SUCCESS, res));
      } else {
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("/")).build();
      }
    } catch (BusinessException e) {
      if (isAjaxRequest(request)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.of(e.getResponseCode(), null));
      } else {
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("/buyer/login?error")).build();
      }
    }
  }

  @PostMapping("/logout")
  public ResponseEntity<?> logout(HttpServletRequest request) {
    HttpSession session = request.getSession(false);
    if (session != null) {
      session.invalidate();
    }

    if (isAjaxRequest(request)) {
      return ResponseEntity.ok(ApiResponse.createSuccess(null, "로그아웃되었습니다."));
    } else {
      return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("/")).build();
    }
  }

  @GetMapping("/info")
  @ResponseBody
  public ApiResponse<Object> getMemberInfo(HttpSession session) {
    Buyer loginBuyer = (Buyer) session.getAttribute(LOGIN_BUYER);
    if (loginBuyer == null) {
      return ApiResponse.of(ApiResponseCode.USER_NOT_AUTHENTICATED, null);
    }

    Optional<Buyer> optionalBuyer = buyerSVC.findMemberInfo(loginBuyer.getBuyerId());
    if (optionalBuyer.isEmpty()) {
      return ApiResponse.of(ApiResponseCode.USER_NOT_FOUND, null);
    }

    BuyerDTO.InfoRes res = new BuyerDTO.InfoRes();
    BeanUtils.copyProperties(optionalBuyer.get(), res);
    return ApiResponse.of(ApiResponseCode.SUCCESS, res);
  }

  @PutMapping("/info")
  public ResponseEntity<?> updateMemberInfo(
      @RequestBody(required = false) BuyerDTO.UpdateReq updateReq,
      @ModelAttribute BuyerDTO.UpdateReq formReq,
      HttpServletRequest request,
      RedirectAttributes redirectAttributes) {

    BuyerDTO.UpdateReq finalReq = (updateReq != null) ? updateReq : formReq;
    HttpSession session = request.getSession(false);
    Buyer loginBuyer = (Buyer) session.getAttribute(LOGIN_BUYER);

    if (loginBuyer == null) {
      return createErrorResponse(request, ApiResponseCode.USER_NOT_AUTHENTICATED, "/buyer/login");
    }

    if (!buyerSVC.checkPassword(loginBuyer.getBuyerId(), finalReq.getCurrentPassword())) {
      return createErrorResponse(request, ApiResponseCode.INVALID_PASSWORD, "/buyer/update?error=password");
    }

    Buyer buyerToUpdate = new Buyer();
    BeanUtils.copyProperties(finalReq, buyerToUpdate);
    buyerSVC.updateMemberInfo(loginBuyer.getBuyerId(), buyerToUpdate);
    
    // Update session
    buyerSVC.findMemberInfo(loginBuyer.getBuyerId()).ifPresent(updatedBuyer -> session.setAttribute(LOGIN_BUYER, updatedBuyer));

    if (isAjaxRequest(request)) {
      return ResponseEntity.ok(ApiResponse.createSuccess(null, "회원정보가 수정되었습니다."));
    } else {
      redirectAttributes.addFlashAttribute("success", "회원정보가 수정되었습니다.");
      return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("/buyer/mypage")).build();
    }
  }

  @DeleteMapping("/leave")
  public ResponseEntity<?> deleteMember(
      @RequestBody(required = false) BuyerDTO.PasswordCheckReq checkReq,
      @ModelAttribute BuyerDTO.PasswordCheckReq formReq,
      HttpServletRequest request) {

    BuyerDTO.PasswordCheckReq finalReq = (checkReq != null) ? checkReq : formReq;
    HttpSession session = request.getSession(false);
    Buyer loginBuyer = (Buyer) session.getAttribute(LOGIN_BUYER);

    if (loginBuyer == null) {
      return createErrorResponse(request, ApiResponseCode.USER_NOT_AUTHENTICATED, "/buyer/login");
    }

    if (!buyerSVC.checkPassword(loginBuyer.getBuyerId(), finalReq.getPassword())) {
      return createErrorResponse(request, ApiResponseCode.INVALID_PASSWORD, "/buyer/leave?error=password");
    }

    buyerSVC.deleteMember(loginBuyer.getBuyerId());
    session.invalidate();

    if (isAjaxRequest(request)) {
      return ResponseEntity.ok(ApiResponse.createSuccess(null, "회원탈퇴가 완료되었습니다."));
    } else {
      return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("/")).build();
    }
  }

  private boolean isAjaxRequest(HttpServletRequest request) {
    String requestedWithHeader = request.getHeader("X-Requested-With");
    if ("XMLHttpRequest".equals(requestedWithHeader)) {
      return true;
    }
    String acceptHeader = request.getHeader("Accept");
    return acceptHeader != null && acceptHeader.contains("application/json");
  }
  
  private ResponseEntity<Object> createErrorResponse(HttpServletRequest request, ApiResponseCode code, String redirectUrl) {
    if (isAjaxRequest(request)) {
      return ResponseEntity.status(code.getStatus()).body(ApiResponse.of(code, null));
    } else {
      return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(redirectUrl)).build();
    }
  }
}
