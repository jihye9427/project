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
     * 메인(홈) 페이지를 보여줍니다.
     * 브라우저에서 / 또는 /home 주소로 GET 요청이 오면 이 메소드가 실행됩니다.
     * @return "home" -> templates/home.html 파일을 찾아 렌더링합니다.
     */
    @GetMapping({"/", "/home"})
    public String home() {
        return "home";
    }

    /**
     * 회원가입 유형 선택 페이지를 보여줍니다.
     * 브라우저에서 /signup 경로로 GET 요청이 오면 이 메소드가 실행됩니다.
     * @return "select_signup" -> templates/select_signup.html 파일을 찾아 렌더링합니다.
     */
    @GetMapping("/signup")
    public String signupSelect() {
        return "select_signup";
    }

    /**
     * 로그인 유형 선택 페이지를 보여줍니다.
     * 브라우저에서 /login 경로로 GET 요청이 오면 이 메소드가 실행됩니다.
     * @return "select_login" -> templates/select_login.html 파일을 찾아 렌더링합니다.
     */
    @GetMapping("/login")
    public String loginSelect() {
        return "select_login";
    }
} 