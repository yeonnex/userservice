package me.yeonnex.userservice.config;

import lombok.RequiredArgsConstructor;
import me.yeonnex.userservice.security.AuthenticationFilter;
import me.yeonnex.userservice.security.AuthorizationFilter;
import me.yeonnex.userservice.service.UserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

/**
 * "인증"이 되어야지만 "권한"부여가 가능해짐
 */
@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final CorsFilter corsFilter; // 모든 요청이 이 필터를 타게 되어있음. 이게 걸려있는 한, cors 요청이 와도 다 허용됨!
    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final Environment env;


    // select pwd from users where email=?
    // db_pwd(encrypted) == input_pwd(encrypted)
    // Authentication (인증 관련)
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService)// select pwd from users where email=?
                .passwordEncoder(bCryptPasswordEncoder);
    }

    // Authorization (권한 관련)
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 사용 X. 이 서버는 stateless 한 서버이다
                .and()
                .addFilter(corsFilter)  // 이 필터를 설정함으로써, cross-origin 요청이 와도 다 허용시킴
                .formLogin().disable() // 폼로그인을 하지 않겠음
                .httpBasic().disable() // Authorization 에 http basic 방식을 쓰지 않겠음. http bearer 방식을 쓰겠음
                .addFilter(new AuthenticationFilter(authenticationManager(), userService, env))// authenticationManager 를 통해 로그인을 진행하기 때문에 인자로 꼭 넘겨주어야 함
                .addFilter(new AuthorizationFilter(authenticationManager(), env))
                .authorizeRequests()
                .antMatchers("/api/v1/user/**")
                .access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/api/v1/manager/**")
                .access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/api/v1/admin/**")
                .access("hasRole('ROLE_ADMIN')")

                .anyRequest().permitAll(); // 그 외 다른 요청들은 권한 없이 들어갈 수 있음!

    }
    /**
     * 이렇게 설정함으로써, 폼태그로 로그인하지 않음
     * 그리고 기본적인 http 로그인 방식을 아예 쓰지 않는다
     * 세션을 만들지도 않음
     *
     * jwt 서버 설정이기 때문
     */

}
