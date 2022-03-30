package me.yeonnex.userservice.security;

import me.yeonnex.userservice.service.UserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * "인증"이 되어야지만 "권한"부여가 가능해짐
 */
@EnableWebSecurity
@Configuration
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private UserService userService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private Environment env;

    // 생성자 주입
    // yml 파일에서 토큰의 유효시간이나, secret key 값을 읽어옴
    public WebSecurity(Environment env, UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder){
        this.env = env;
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }
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
//        http.authorizeRequests().antMatchers("/users/**").permitAll();
        http.authorizeRequests()
                .antMatchers("/users/**").permitAll() // 회원 가입 요청은 퍼밋함
                .anyRequest()
                .authenticated()
                .and()
                .addFilter(getAuthenticationFilter()); // 이 필터를 통과한 데이터만 권한을 부여하고 계속 작업을 진행하겠다!
    }

    private AuthenticationFilter getAuthenticationFilter() throws Exception {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManager(), userService, env);
//        authenticationFilter.setAuthenticationManager(authenticationManager()); // 인증 처리를 해주는 애(authenticationManager). 스프링 시큐리티에서 가져옴
        // 로그인 처리 관련 쿼리 이런거 만들지 않았음. 스프링 시큐리티의 로그인 기능(authenticationManager)을 사용했기 때문!
        return authenticationFilter;
    }

}
