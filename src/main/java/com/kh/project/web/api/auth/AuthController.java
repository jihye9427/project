package com.kh.project.web.api.auth;

import com.kh.project.domain.common.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    public static final String LOGIN_MEMBER = "loginUser";
    public static final String MEMBER_TYPE = "userType";

    @GetMapping("/status")
    public ApiResponse<Object> getLoginStatus(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session != null && session.getAttribute(LOGIN_MEMBER) != null) {
            Object user = session.getAttribute(LOGIN_MEMBER);
            String userType = (String) session.getAttribute(MEMBER_TYPE);

            Map<String, Object> loginInfo = new HashMap<>();
            loginInfo.put("user", user);
            loginInfo.put("userType", userType);

            return ApiResponse.create("0", "로그인 상태입니다.", loginInfo);
        }

        return ApiResponse.create("99", "로그인되지 않았습니다.", null);
    }

    @PostMapping("/logout")
    public ApiResponse<Object> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        return ApiResponse.create("0", "로그아웃되었습니다.", null);
    }
} 