package com.kh.project.config;

import com.kh.project.web.interceptor.AuthInterceptor; // 인터셉터 import
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // CORS 설정 (변경 없음)
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**").allowedOrigins("http://127.0.0.1:5500").allowCredentials(true);
    }

    // ViewController 설정: 로직이 없는 단순 페이지를 Controller 없이 매핑
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("home"); // / 요청은 home.html로
        registry.addViewController("/home").setViewName("home");
        
        // 구매자/판매자 회원가입 폼은 단순 페이지이므로 여기서 처리
        registry.addViewController("/buyer/joinForm").setViewName("buyer/buyer_signup");
        registry.addViewController("/seller/joinForm").setViewName("seller/seller_signup");
        
        // 로그인 선택, 회원가입 선택 페이지
        registry.addViewController("/login").setViewName("select_login");
        registry.addViewController("/signup").setViewName("select_signup");
    }

    // [핵심] 로그인 체크 인터셉터 등록
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthInterceptor())
                .addPathPatterns("/**") // 모든 경로에 대해 인터셉터를 적용
                .excludePathPatterns(    // 하지만 아래 경로들은 검사에서 제외
                        "/", "/home", "/login", "/signup",
                        "/buyer/login", "/buyer/join", "/buyer/joinForm",
                        "/seller/login", "/seller/join", "/seller/joinForm",
                        "/api/**",       // 모든 API 호출
                        "/css/**", "/js/**", "/images/**"
                );
    }
} 