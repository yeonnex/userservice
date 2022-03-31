package me.yeonnex.userservice.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.yeonnex.userservice.auth.PrincipalDetails;
import me.yeonnex.userservice.dto.UserDto;
import me.yeonnex.userservice.service.UserService;
import me.yeonnex.userservice.vo.RequestLogin;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 * 2개의 메서드를 재정의할 것인데,
 * 로그인 시도시 필터링 - attemptAuthentication 메서드에서.
 * 로그인 성공시  - successfulAuthentication 메서드에서.
 *
 * UsernamePasswordAuthenticationToken ?
 * 사용자가 입력한 이메일과 비밀번호 값을 스프링 시큐리티에서 사용할 수
 * 있는 값으로 변환하기 위해 존재. 이 타입으로 만들어주어야 한다.
 * */
@Slf4j
@RequiredArgsConstructor
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final UserService userService;
    private final Environment env;
    private final AuthenticationManager authenticationManager;


    // 로그인 시도 시 "가장 먼저" 실행되는 함수. 중단점 찍어서 확인해보기 📌
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        log.info("AuthenticationFilter: 로그인 시도중");
        try {
            RequestLogin credential = new ObjectMapper().readValue(request.getInputStream(), RequestLogin.class);
            log.info(String.valueOf(credential)); // RequestLogin(email="moomoo@naver.com", password="1234")
            // 이제 사용자가 입력한 로그인 정보를 받았으니, 이것을 가지고 인증정보를 만들어보자
            // 이를 위해서는 UsernamePasswordAuthenticationFilter 에 전달을 해주어야 한다
            // -> UsernamePasswordAuthentication"Token" 으로 변경해줘야 한다!
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(credential.getEmail(), credential.getPassword());
            /*
                PrincipalDetailsService 의 loadUserByUsername 함수가 실행된 후 정상이면 authentication 이 리턴됨.
             */
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            log.info("로그인 안료됨: " + PrincipalDetails.getAccount().getUsername()); // 로그인이 정상적으로 되었다는 뜻

            // authentication 객체가 시큐리티 영역에 저장이 되어야 하는데, 그 방법이 return 해주는 것.
            return authentication;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 사용자가 입력한 이메일과 PW가 실제 있을때. 즉 로그인에 성공했을 때 이 함수가 실행됨
    // 여기서 토큰을 만들거나, 로그인시 반환값 등등등 의 작업을 여기에!
   @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        log.info("로그인 성공!!!✨🎊🎉");

        // User 는 스프링 시큐리티의 타입
        log.debug(((User)authResult.getPrincipal()).getUsername()); // 함수 이름이 getUsername 이어서 좀 그렇긴 하지만 실제로는 이메일이 출력된다.
       String userEmail = ((User)authResult.getPrincipal()).getUsername(); // "mooomoo@naver.com"
       UserDto userDetailsByEmail = userService.getUserDetailsByEmail(userEmail);
       String token = Jwts.builder()
               .setSubject(userDetailsByEmail.getUserId())
               .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(env.getProperty("token.expiration_time"))))
               .signWith(SignatureAlgorithm.HS256, env.getProperty("token.secret"))
               .compact(); // 토큰 완성 ! 🎊
       log.info("토큰 생성 완료 🎊");
       System.out.println(token);
       System.out.println(new Date(System.currentTimeMillis() + Long.parseLong(env.getProperty("token.expiration_time"))));
       System.out.println(new Date(System.currentTimeMillis()));
       System.out.println(Long.parseLong(env.getProperty("token.expiration_time")));

       response.addHeader("token", token);
       response.addHeader("userId", userDetailsByEmail.getUserId()); // 나중에 이 토큰이 정상적으로 만들어진 것인지 확인해보기 위한 값으로써 userId 전달할 것임

    }
}
