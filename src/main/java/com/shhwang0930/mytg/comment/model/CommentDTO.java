package com.shhwang0930.mytg.comment.model;

import com.shhwang0930.mytg.board.model.BoardEntity;
import com.shhwang0930.mytg.user.model.UserEntity;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class CommentDTO {
    private String desc;
    private String user;
    private Long board;

    public static CommentDTO fromEntity(CommentEntity comment){
        return CommentDTO.builder()
                .desc(comment.getCommentDesc())
                .board(comment.getBoard().getIdx())
                .user(comment.getUser().getUsername())
                .build();
    }

    public static List<CommentDTO> fromEntityList(List<CommentEntity> commentList){
        return commentList.stream()
                .map(CommentDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public CommentEntity toEntity(BoardEntity boardEntity ,UserEntity userEntity) {
        return CommentEntity.builder()
                .commentDesc(this.desc)
                .board(boardEntity)
                .user(userEntity)
                .build();
    }
}
