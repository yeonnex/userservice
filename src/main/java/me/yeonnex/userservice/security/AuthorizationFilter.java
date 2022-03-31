package me.yeonnex.userservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Enumeration;



@Slf4j
public class AuthorizationFilter extends BasicAuthenticationFilter {

    private Environment env;

    public AuthorizationFilter(AuthenticationManager authenticationManager, Environment env){
        super(authenticationManager);
        this.env = env;
    }



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info(request.getHeader(HttpHeaders.AUTHORIZATION));
        if(request.getHeader("AUTHORIZATION").length()==0){
            onError(response, "No authorization header...", HttpStatus.UNAUTHORIZED);
        }
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String jwt = authorizationHeader.replace("Bearer ", "");
        log.info(jwt);
        if(!isJwtValid(jwt)){
            onError(response,"token is not valid", HttpStatus.UNAUTHORIZED);
        }
        chain.doFilter(request, response);
    }

    private boolean isJwtValid(String jwt) {

        try {
            log.info("try 실행");
            System.out.println(env.getProperty("token.secret"));
            /*String subject = Jwts.parser()
                    .setSigningKey(env.getProperty("token.secret").getBytes("UTF-8")) // Set Key
                    .parseClaimsJws(jwt) // 파싱 및 검증, 실패 시 에러
                    .getBody().getSubject();*/
            Jws<Claims> claims = Jwts.parser()
                            .setSigningKey(env.getProperty("token.secret").getBytes(StandardCharsets.UTF_8))
                                    .parseClaimsJws(jwt);
            log.info("로그출력");

            return true;
        }catch (ExpiredJwtException e){ // 토큰이 만료되었을 경우
            System.out.println(e);
            return false;
        }catch (Exception e){ // 그외 에러났을 경우
            log.info("유효하지 않은 토큰입니다");
            System.out.println(e);
            return false;
        }

    }

    private HttpServletResponse onError(HttpServletResponse response, String err, HttpStatus unauthorized) {
        response.setStatus(401); // 권한없음 상태코드
        log.error(err);
        return response;
    }
}
