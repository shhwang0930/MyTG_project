package com.shhwang0930.mytg.comment.domain;

import com.shhwang0930.mytg.board.model.entity.BoardEntity;
import com.shhwang0930.mytg.board.model.entity.Category;
import com.shhwang0930.mytg.comment.model.CommentEntity;
import com.shhwang0930.mytg.user.model.entity.UserEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CommentDomainTest {
    @Test
    @DisplayName("comment 생성 테스트")
    void createBoard(){
        //given
        UserEntity user = new UserEntity();
        user.setUsername("test user");
        user.setPassword("test pw");
        user.setRole("ADMIN");

        Category category = Category.정보처리기사;

        BoardEntity board =
                BoardEntity
                        .builder()
                        .title("test title")
                        .content("test content")
                        .user(user)
                        .category(category)
                        .build();

        CommentEntity comment =
                CommentEntity
                        .builder()
                        .board(board)
                        .commentDesc("test desc")
                        .user(user)
                        .build();

        //when, then
        Assertions.assertThat(comment.getBoard()).isEqualTo(board);
        Assertions.assertThat(comment.getCommentDesc()).isEqualTo("test desc");
        Assertions.assertThat(comment.getUser()).isEqualTo(user);
    }
}
