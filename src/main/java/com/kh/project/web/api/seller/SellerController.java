package com.kh.project.web.api.seller;

import com.kh.project.domain.common.ApiResponse;
import com.kh.project.domain.common.ApiResponseCode;
import com.kh.project.domain.common.LoginDTO;
import com.kh.project.domain.exception.BusinessException;
import com.kh.project.domain.seller.entity.Seller;
import com.kh.project.domain.seller.svc.SellerSVC;
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
@RequestMapping("/seller")
public class SellerController {
  private final SellerSVC sellerSVC;
  public static final String LOGIN_SELLER = "loginSeller";

  // --- 페이지 로딩 메소드 ---
  @GetMapping("/login") public String loginForm() { return "seller/seller_login"; }
  @GetMapping("/signup") public String signupForm() { return "seller/seller_signup"; }
  @GetMapping("/mypage") public String mypage() { return "seller/seller_mypage"; }
  @GetMapping("/update") public String updateForm() { return "seller/seller_update"; }
  @GetMapping("/leave") public String leaveForm() { return "seller/seller_leave"; }
  @GetMapping({"/", "/index"}) public String index() { return "seller/seller_index"; }


  // --- 데이터 처리 메소드 (콘텐츠 협상) ---

  @PostMapping("/join")
  public ResponseEntity<?> join(
          @Valid @RequestBody(required = false) SellerDTO.JoinReq jsonReq,
          @ModelAttribute SellerDTO.JoinReq formReq,
          HttpServletRequest request) {

    SellerDTO.JoinReq joinReq = (jsonReq != null) ? jsonReq : formReq;
    Seller seller = new Seller();
    BeanUtils.copyProperties(joinReq, seller);
    seller.setGubun("MD102");

    try {
      Seller joinedSeller = sellerSVC.join(seller);
      if (isAjaxRequest(request)) {
        return ResponseEntity.ok(ApiResponse.of(ApiResponseCode.SUCCESS, joinedSeller.getSellerId()));
      } else {
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("/seller/login")).build();
      }
    } catch (BusinessException e) {
      if (isAjaxRequest(request)) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.of(e.getResponseCode(), null));
      } else {
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("/seller/signup?error")).build();
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
      Seller loginSeller = sellerSVC.login(loginDTO.getEmail(), loginDTO.getPassword());
      request.getSession(true).setAttribute(LOGIN_SELLER, loginSeller);

      if (isAjaxRequest(request)) {
        SellerDTO.InfoRes res = new SellerDTO.InfoRes();
        BeanUtils.copyProperties(loginSeller, res);
        return ResponseEntity.ok(ApiResponse.of(ApiResponseCode.SUCCESS, res));
      } else {
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("/")).build();
      }
    } catch (BusinessException e) {
      if (isAjaxRequest(request)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.of(e.getResponseCode(), null));
      } else {
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("/seller/login?error")).build();
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
    Seller loginSeller = (Seller) session.getAttribute(LOGIN_SELLER);
    if (loginSeller == null) {
      return ApiResponse.of(ApiResponseCode.USER_NOT_AUTHENTICATED, null);
    }

    Optional<Seller> optionalSeller = sellerSVC.findMemberInfo(loginSeller.getSellerId());
    if (optionalSeller.isEmpty()) {
      return ApiResponse.of(ApiResponseCode.USER_NOT_FOUND, null);
    }

    SellerDTO.InfoRes res = new SellerDTO.InfoRes();
    BeanUtils.copyProperties(optionalSeller.get(), res);
    return ApiResponse.of(ApiResponseCode.SUCCESS, res);
  }

  @PutMapping("/info")
  public ResponseEntity<?> updateMemberInfo(
          @RequestBody(required = false) SellerDTO.UpdateReq updateReq,
          @ModelAttribute SellerDTO.UpdateReq formReq,
          HttpServletRequest request,
          RedirectAttributes redirectAttributes) {

    SellerDTO.UpdateReq finalReq = (updateReq != null) ? updateReq : formReq;
    HttpSession session = request.getSession(false);
    Seller loginSeller = (Seller) session.getAttribute(LOGIN_SELLER);

    if (loginSeller == null) {
      return createErrorResponse(request, ApiResponseCode.USER_NOT_AUTHENTICATED, "/seller/login");
    }

    if (!sellerSVC.checkPassword(loginSeller.getSellerId(), finalReq.getCurrentPassword())) {
      return createErrorResponse(request, ApiResponseCode.INVALID_PASSWORD, "/seller/update?error=password");
    }

    Seller sellerToUpdate = new Seller();
    BeanUtils.copyProperties(finalReq, sellerToUpdate);
    sellerSVC.updateMemberInfo(loginSeller.getSellerId(), sellerToUpdate);

    sellerSVC.findMemberInfo(loginSeller.getSellerId()).ifPresent(updatedSeller -> session.setAttribute(LOGIN_SELLER, updatedSeller));

    if (isAjaxRequest(request)) {
      return ResponseEntity.ok(ApiResponse.createSuccess(null, "회원정보가 수정되었습니다."));
    } else {
      redirectAttributes.addFlashAttribute("success", "회원정보가 수정되었습니다.");
      return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("/seller/mypage")).build();
    }
  }

  @DeleteMapping("/leave")
  public ResponseEntity<?> deleteMember(
          @RequestBody(required = false) SellerDTO.PasswordCheckReq checkReq,
          @ModelAttribute SellerDTO.PasswordCheckReq formReq,
          HttpServletRequest request) {

    SellerDTO.PasswordCheckReq finalReq = (checkReq != null) ? checkReq : formReq;
    HttpSession session = request.getSession(false);
    Seller loginSeller = (Seller) session.getAttribute(LOGIN_SELLER);

    if (loginSeller == null) {
      return createErrorResponse(request, ApiResponseCode.USER_NOT_AUTHENTICATED, "/seller/login");
    }

    if (!sellerSVC.checkPassword(loginSeller.getSellerId(), finalReq.getPassword())) {
      return createErrorResponse(request, ApiResponseCode.INVALID_PASSWORD, "/seller/leave?error=password");
    }

    sellerSVC.deleteMember(loginSeller.getSellerId());
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
