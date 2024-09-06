package com.shhwang0930.mytg.board.domain;

import com.shhwang0930.mytg.board.model.entity.BoardEntity;
import com.shhwang0930.mytg.board.model.entity.Category;
import com.shhwang0930.mytg.user.model.entity.UserEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BoardDomainTest {
    @Test
    @DisplayName("board 생성 테스트")
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

        //when, then
        Assertions.assertThat(board.getTitle()).isEqualTo("test title");
        Assertions.assertThat(board.getContent()).isEqualTo("test content");
        Assertions.assertThat(board.getUser()).isEqualTo(user);
        Assertions.assertThat(board.getCategory()).isEqualTo(Category.정보처리기사);
    }
}
