package com.shhwang0930.mytg.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shhwang0930.mytg.common.model.ResponseMessage;
import com.shhwang0930.mytg.common.model.StatusCode;
import com.shhwang0930.mytg.jwt.JWTUtil;
import com.shhwang0930.mytg.jwt.service.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static com.shhwang0930.mytg.jwt.constants.AuthConst.REFRESH_TOKEN_TYPE;

public class CustomLogoutFilter extends GenericFilterBean {

    private final JWTUtil jwtUtil;
    private final JwtService jwtService;

    public CustomLogoutFilter(JWTUtil jwtUtil, JwtService jwtService) {

        this.jwtUtil = jwtUtil;
        this.jwtService = jwtService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        //path and method verify
        String requestUri = request.getRequestURI();
        if (!requestUri.matches("^\\/logout$")) {

            filterChain.doFilter(request, response);
            return;
        }
        String requestMethod = request.getMethod();
        if (!requestMethod.equals("POST")) {
            filterChain.doFilter(request, response);
            return;
        }

        ObjectMapper objectMapper = new ObjectMapper();

        //get refresh token
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        if(cookies == null){
            StatusCode statusCode = StatusCode.TOKEN_NOT_FOUND;
            ResponseMessage responseMessage = new ResponseMessage(statusCode.getCode(), statusCode.getMessage(),null);

            //JSON 형태로 반환
            String body = objectMapper.writeValueAsString(responseMessage);
            PrintWriter writer = response.getWriter();
            writer.print(body);

            response.setStatus(HttpStatus.OK.value());
            return;
        }
        for (Cookie cookie : cookies) {

            if (cookie.getName().equals(REFRESH_TOKEN_TYPE)) {
                refresh = cookie.getValue();
            }
        }

        //refresh null check
        if (refresh == null) {
            StatusCode statusCode = StatusCode.TOKEN_NOT_FOUND;
            ResponseMessage responseMessage = new ResponseMessage(statusCode.getCode(), statusCode.getMessage(),null);

            //JSON 형태로 반환
            String body = objectMapper.writeValueAsString(responseMessage);
            PrintWriter writer = response.getWriter();
            writer.print(body);

            response.setStatus(HttpStatus.OK.value());
            return;
        }

        //expired check
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            StatusCode statusCode = StatusCode.TOKEN_NOT_FOUND;
            ResponseMessage responseMessage = new ResponseMessage(statusCode.getCode(), statusCode.getMessage(),null);

            //JSON 형태로 반환
            String body = objectMapper.writeValueAsString(responseMessage);
            PrintWriter writer = response.getWriter();
            writer.print(body);

            response.setStatus(HttpStatus.OK.value());
            return;
        }

        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(refresh);
        if (!category.equals(REFRESH_TOKEN_TYPE)) {

            StatusCode statusCode = StatusCode.INVAILD_TOKEN;
            ResponseMessage responseMessage = new ResponseMessage(statusCode.getCode(), statusCode.getMessage(),null);

            //JSON 형태로 반환
            String body = objectMapper.writeValueAsString(responseMessage);
            PrintWriter writer = response.getWriter();
            writer.print(body);

            response.setStatus(HttpStatus.OK.value());
            return;
        }

        //DB에 저장되어 있는지 확인
        Boolean isExist = jwtService.isExistRefreshToken(refresh);
        if (!isExist) {
            StatusCode statusCode = StatusCode.TOKEN_NOT_FOUND;
            ResponseMessage responseMessage = new ResponseMessage(statusCode.getCode(), statusCode.getMessage(),null);

            //JSON 형태로 반환
            String body = objectMapper.writeValueAsString(responseMessage);
            PrintWriter writer = response.getWriter();
            writer.print(body);

            response.setStatus(HttpStatus.OK.value());
            return;
        }

        //로그아웃 진행
        //Refresh 토큰 DB에서 제거
        jwtService.deleteRefreshToken(refresh);

        //Refresh 토큰 Cookie 값 0
        Cookie cookie = new Cookie(REFRESH_TOKEN_TYPE, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");

        response.addCookie(cookie);

        StatusCode statusCode = StatusCode.SUCCESS;
        ResponseMessage responseMessage = new ResponseMessage(statusCode.getCode(),statusCode.getMessage(),null);

        //JSON 형태로 반환
        String body = objectMapper.writeValueAsString(responseMessage);
        PrintWriter writer = response.getWriter();
        writer.print(body);

        response.setStatus(HttpStatus.OK.value());
    }
}
