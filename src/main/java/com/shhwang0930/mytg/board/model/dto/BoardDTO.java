package com.shhwang0930.mytg.board.model.dto;

import com.shhwang0930.mytg.board.model.entity.BoardEntity;
import com.shhwang0930.mytg.board.model.entity.Category;
import com.shhwang0930.mytg.user.model.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardDTO {
    private String title;
    private String content;
    private String category;
    private String user;

    public static BoardDTO fromEntity(BoardEntity board){
        return BoardDTO.builder()
                .title(board.getTitle())
                .content(board.getContent())
                .category(board.getCategory().name()) // ENUM to String
                .user(board.getUser().getUsername())
                .build();
    }

    public static List<BoardDTO> fromEntityList(List<BoardEntity> boardList){
        return boardList.stream()
                .map(BoardDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public BoardEntity toEntity(UserEntity userEntity) {
        return BoardEntity.builder()
                .title(this.title)
                .content(this.content)
                .category(Category.valueOf(this.category))
                .user(userEntity)
                .build();
    }


}
