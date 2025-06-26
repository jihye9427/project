package com.kh.project.web.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {// 메인 페이지
  @GetMapping("/")
  public String index() {
    return "index";
  }

  // 회원가입 유형 선택
  @GetMapping("/signup")
  public String signupSelect() {
    return "select_signup";
  }

  // 로그인 유형 선택
  @GetMapping("/login")
  public String loginSelect() {
    return "select_login";
  }
}
