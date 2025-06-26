package com.kh.project.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // CORS 설정 (변경 없음)
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://127.0.0.1:5500", "http://localhost:5500")
                .allowedMethods("*")
                .allowCredentials(true);
    }

    // 정적 리소스 경로 설정 (추가)
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }

    // 로그인 체크 인터셉터 등록
    // @Override
    // public void addInterceptors(InterceptorRegistry registry) {
    //     registry.addInterceptor(new AuthInterceptor())
    //             .addPathPatterns("/**")
    //             .excludePathPatterns(
    //                     "/", "/home", "/login", "/signup",
    //                     "/buyer/login", "/buyer/join",
    //                     "/seller/login", "/seller/join",
    //                     "/api/**",
    //                     "/css/**", "/js/**", "/images/**", "/error"
    //             );
    // }
}