package com.kh.project.web.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();

        // 현재 세션을 가져옴 (없으면 null)
        HttpSession session = request.getSession(false);

        // 세션이 없거나, 세션에 로그인 정보가 없으면
        if (session == null || (session.getAttribute("loginBuyer") == null && session.getAttribute("loginSeller") == null)) {
            // 로그인 페이지로 리다이렉트
            response.sendRedirect("/login?redirectURL=" + requestURI);
            return false; // 컨트롤러 실행 중단
        }

        return true; // 로그인 되어 있으면 컨트롤러 실행
    }
} 