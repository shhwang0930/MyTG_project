package com.shhwang0930.mytg.common.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusCode {
    //성공
    SUCCESS(2000, "성공"),

    //게시글 관련 에러코드
    BOARD_NOT_FOUND(4001,"게시글이 없습니다."),

    //댓글 관련 에러코드
    COMMENT_NOT_FOUND(4002,"댓글이 없습니다."),
    BOARD_COMMENT_NOT_MATCH(4003,"해당 게시글의 댓글이 아닙니다."),

    //유저 관련 에러코드
    USER_MATCH_FAILED(4004,"유저 정보가 일치하지 않습니다."),
    RESOURCE_ALREADY_EXISTS(4005,"이미 존재하는 정보입니다."),
    LOGIN_FAILED(4006,"로그인이 실패하였습니다."),
    FIND_USER_FAILED(4007,"유저 정보를 찾을 수 없습니다."),

    //JWT 관련 에러코드
    TOKEN_NOT_FOUND(4008,"JWT 토큰이 존재하지 않습니다."),
    INVAILD_TOKEN(4009,"올바르지 않은 토큰입니다."),

    //유저 시험 정보 관련 코드
    USER_EXAM_NOT_FOUND(4010,"유저의 등록된 시험 정보가 없습니다.");

    private final Integer code;
    private final String message;
}
