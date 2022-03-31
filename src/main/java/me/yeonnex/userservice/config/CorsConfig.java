package me.yeonnex.userservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 외부에서 오는 요청을 허용하기 위한 필터
 */
@Configuration
public class CorsConfig {
    // 얘를 이렇게 적어놓는 것만으로는 의미가 없고, "필터에 등록"을 해줘야 한다
    @Bean
    public CorsFilter corsFilter(){ // 스프링 프레임워크가 들고있는 CorsFilter 여야 한다
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true); // 내 서버가 응답(json)을 할때 자바스크립트에서 처리할 수 있게 하겠다
        config.addAllowedMethod("*"); // 모든 ip에 응답을 허용하겠다
        config.addAllowedHeader("*"); // 모든 header 에 응답을 허용하겠다
        config.addAllowedMethod("*"); // 모든 post, get, put, delete, patch 요청을 허용하겠다

        source.registerCorsConfiguration("/api/**", config); // /api/** 로 들어오는 모든 주소는 이 config 설정을 따르라

        return new CorsFilter(source);

    }
}
