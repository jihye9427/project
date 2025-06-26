package com.kh.project.web.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 메인 페이지, 로그인/회원가입 선택 등
 * 최상위 경로의 공용 페이지 이동을 담당하는 컨트롤러
 */
@Controller
public class HomeController {

    /**
     * 메인화면 페이지
     * 브라우저에서 / 또는 /home 주소로 GET 요청이 오면 실행됨.
     * @return "home" -> templates/home.html 파일을 찾아 렌더링
     */
    @GetMapping({"/", "/home"})
    public String home() {
        return "home";
    }

    /**
     * 회원가입 유형 선택 페이지(구매자, 판매자)
     * 브라우저에서 /signup 경로로 GET 요청이 오면 실행됨.
     * @return "select_signup" -> templates/select_signup.html 파일을 찾아 렌더링
     */
    @GetMapping("/signup")
    public String signupSelect() {
        return "select_signup";
    }
} 