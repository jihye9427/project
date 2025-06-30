package com.kh.project.config;

import com.kh.project.web.interceptor.AuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired(required = false)
    private AuthInterceptor authInterceptor;

    // CORS 설정 (API 통신용)
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // API 요청에만 CORS 적용
            .allowedOrigins("http://127.0.0.1:5500", "http://localhost:5500")
            .allowedMethods("*")
            .allowCredentials(true);
    }

    /**
     * 정적 리소스는 특정 경로만 지정하여 Controller 경로와 충돌을 막습니다.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/css/");
        registry.addResourceHandler("/js/**").addResourceLocations("classpath:/static/js/");
        registry.addResourceHandler("/images/**").addResourceLocations("classpath:/static/images/");
    }

    /**
     * 로직이 없는 단순 페이지에 대해 Controller 없이 URL과 뷰(HTML)를 직접 매핑합니다.
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 공용 페이지
        registry.addViewController("/").setViewName("home");
        registry.addViewController("/home").setViewName("home");
        registry.addViewController("/login").setViewName("common/select_login");
        registry.addViewController("/signup").setViewName("common/select_signup");

        // 구매자 폼 페이지
        registry.addViewController("/buyer/login").setViewName("buyer/buyer_login");
        registry.addViewController("/buyer/signup").setViewName("buyer/buyer_signup");
        registry.addViewController("/buyer/mypage").setViewName("buyer/buyer_mypage");
        registry.addViewController("/buyer/leave").setViewName("buyer/buyer_leave");

        // 판매자 폼 페이지
        registry.addViewController("/seller/login").setViewName("seller/seller_login");
        registry.addViewController("/seller/signup").setViewName("seller/seller_signup");
        registry.addViewController("/seller/mypage").setViewName("seller/seller_mypage");
        registry.addViewController("/seller/leave").setViewName("seller/seller_leave");
    }

    // 인터셉터 설정은 필요 시 주석 해제 후 사용
    /*
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**", "/js/**", "/images/**", "/", "/home", "/login", "/signup", "/api/**");
    }
    */
}