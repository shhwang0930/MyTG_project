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
    RESOURCE_ALREADY_EXISTS(4005,"이미 존재하는 정보입니다.");

    private final Integer code;
    private final String message;
}
