package com.shhwang0930.mytg.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shhwang0930.mytg.common.model.ResponseMessage;
import com.shhwang0930.mytg.common.model.StatusCode;
import com.shhwang0930.mytg.jwt.JWTUtil;
import com.shhwang0930.mytg.jwt.service.JwtService;
import jdk.jshell.Snippet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;

import static com.shhwang0930.mytg.jwt.constants.AuthConst.*;

@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    private final JwtService jwtService;

    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, JwtService jwtService) {

        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.jwtService = jwtService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        //클라이언트 요청에서 username, password 추출
        String username = obtainUsername(request);
        String password = obtainPassword(request);

        //스프링 시큐리티에서 username과 password를 검증하기 위해서는 token에 담아야 함 << dto의 역할을 함
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);

        //token에 담은 검증을 위한 AuthenticationManager로 전달
        try {
            return authenticationManager.authenticate(authToken);
        }catch (InternalAuthenticationServiceException e){
            //내부 인증 서비스 예외 발생 처리
            log.error("내부 인증 서비스 예외 발생: {}", e.getMessage());
            throw new AuthenticationServiceException("Internal authentication service exception", e);
        }
    }

    //로그인 성공시 실행하는 메소드 (여기서 JWT를 발급)
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException {
        //user info
        String username = authentication.getName();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        //토큰 생성
        String access = jwtUtil.createJwt(ACCESS_TOKEN_TYPE, username, role, ACCESS_TOKEN_EXPIRED_MS);
        String refresh = jwtUtil.createJwt(REFRESH_TOKEN_TYPE, username, role, REFRESH_TOKEN_EXPIRED_MS);

        //Refresh 토큰 저장
        jwtService.saveRefreshToken(refresh,username);

        //응답 설정
        response.setHeader(ACCESS_TOKEN_TYPE, access);
        response.addCookie(createCookie(REFRESH_TOKEN_TYPE, refresh));

        ObjectMapper objectMapper = new ObjectMapper();
        StatusCode statusCode = StatusCode.SUCCESS;
        ResponseMessage responseMessage = new ResponseMessage(statusCode.getCode(), statusCode.getMessage(), null);

        //JSON 형태로 반환
        String body = objectMapper.writeValueAsString(responseMessage);
        PrintWriter writer = response.getWriter();
        writer.print(body);

        response.setStatus(HttpStatus.OK.value());
    }



    //로그인 실패시 실행하는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        ObjectMapper objectMapper = new ObjectMapper();
        StatusCode statusCode = StatusCode.LOGIN_FAILED;
        ResponseMessage responseMessage = new ResponseMessage(statusCode.getCode(), statusCode.getMessage(), null);

        String body = objectMapper.writeValueAsString(responseMessage);

        PrintWriter writer = response.getWriter();
        writer.print(body);

        response.setStatus(HttpServletResponse.SC_OK);
    }


    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        //cookie.setSecure(true); //https 통신일 경우
        //cookie.setPath("/"); //쿠키가 적용될 범위
        cookie.setHttpOnly(true);

        return cookie;
    }
}
