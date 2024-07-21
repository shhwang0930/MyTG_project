package com.shhwang0930.mytg.jwt.filter;

import com.shhwang0930.mytg.jwt.JWTUtil;
import com.shhwang0930.mytg.jwt.service.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import static com.shhwang0930.mytg.jwt.constants.AuthConst.*;

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

        System.out.println(username);

        //스프링 시큐리티에서 username과 password를 검증하기 위해서는 token에 담아야 함 << dto의 역할을 함
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);

        //token에 담은 검증을 위한 AuthenticationManager로 전달
        return authenticationManager.authenticate(authToken);
    }

    //로그인 성공시 실행하는 메소드 (여기서 JWT를 발급)
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {
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
        response.setStatus(HttpStatus.OK.value());
    }



    //로그인 실패시 실행하는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setStatus(401);
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
