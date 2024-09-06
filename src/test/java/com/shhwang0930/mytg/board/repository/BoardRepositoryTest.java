package com.shhwang0930.mytg.board.repository;

import com.shhwang0930.mytg.board.model.entity.BoardEntity;
import com.shhwang0930.mytg.board.model.entity.Category;
import com.shhwang0930.mytg.config.TestConfig;
import com.shhwang0930.mytg.user.model.entity.UserEntity;
import com.shhwang0930.mytg.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;


@DataJpaTest
@Import(TestConfig.class)
public class BoardRepositoryTest {
    @Autowired
    BoardRepository boardRepository;
    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("게시글 생성")
    void createBoard(){
        //given
        UserEntity user1 = new UserEntity();
        user1.setUsername("user1");
        user1.setPassword("pw1");
        user1.setRole("ADMIN");

        UserEntity user2 = new UserEntity();
        user2.setUsername("user2");
        user2.setPassword("pw2");
        user2.setRole("ADMIN");

        Category category = Category.정보처리기사;
        BoardEntity board1 =
                BoardEntity
                        .builder()
                        .title("title 1")
                        .content("content 1")
                        .user(user1)
                        .category(category)
                        .build();

        BoardEntity board2 =
                BoardEntity
                        .builder()
                        .title("title 2")
                        .content("content 2")
                        .user(user2)
                        .category(category)
                        .build();

        //when
        userRepository.save(user1);
        userRepository.save(user2);
        BoardEntity result1 = boardRepository.save(board1);
        BoardEntity result2 = boardRepository.save(board2);

        //then
        Assertions.assertThat(result1.getTitle()).isEqualTo(board1.getTitle());
        Assertions.assertThat(result2.getTitle()).isEqualTo(board2.getTitle());
        Assertions.assertThat(result1.getContent()).isEqualTo(board1.getContent());
        Assertions.assertThat(result2.getContent()).isEqualTo(board2.getContent());
        Assertions.assertThat(result1.getUser()).isEqualTo(board1.getUser());
        Assertions.assertThat(result2.getUser()).isEqualTo(board2.getUser());
    }

    @Test
    @DisplayName("게시글 리스트")
    void boardList(){
        //given
        UserEntity user1 = new UserEntity();
        user1.setUsername("user1");
        user1.setPassword("pw1");
        user1.setRole("ADMIN");
        UserEntity user2 = new UserEntity();
        user2.setUsername("user2");
        user2.setPassword("pw2");
        user2.setRole("ADMIN");

        Category category = Category.정보처리기사;
        BoardEntity board1 =
                BoardEntity
                        .builder()
                        .title("title 1")
                        .content("content 1")
                        .user(user1)
                        .category(category)
                        .build();

        BoardEntity board2 =
                BoardEntity
                        .builder()
                        .title("title 2")
                        .content("content 2")
                        .user(user2)
                        .category(category)
                        .build();

        userRepository.save(user1);
        userRepository.save(user2);
        BoardEntity result1 = boardRepository.save(board1);
        BoardEntity result2 = boardRepository.save(board2);

        //when
        List<BoardEntity> result = boardRepository.findAll();

        //then
        Assertions.assertThat(result.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("게시글 상세보기")
    void boardFind(){
        //given
        UserEntity user1 = new UserEntity();
        user1.setUsername("user1");
        user1.setPassword("pw1");
        user1.setRole("ADMIN");

        Category category = Category.정보처리기사;
        BoardEntity board1 =
                BoardEntity
                        .builder()
                        .title("title 1")
                        .content("content 1")
                        .user(user1)
                        .category(category)
                        .build();

        userRepository.save(user1);
        boardRepository.save(board1);

        //when
        BoardEntity result = boardRepository.findAllByIdx(1L);

        //then
        Assertions.assertThat(result).isEqualTo(board1);
    }

    @Test
    @DisplayName("게시글 존재유무")
    void boardIsExists(){
        //given
        UserEntity user1 = new UserEntity();
        user1.setUsername("user1");
        user1.setPassword("pw1");
        user1.setRole("ADMIN");

        Category category = Category.정보처리기사;
        BoardEntity board1 =
                BoardEntity
                        .builder()
                        .idx(1L) // idx 지정을 해줘야 함
                        .title("title 1")
                        .content("content 1")
                        .user(user1)
                        .category(category)
                        .build();

        userRepository.save(user1);
        boardRepository.save(board1);

        //when
        Boolean result = boardRepository.existsByIdx(1L);

        //then
        Assertions.assertThat(result).isEqualTo(true);
    }

    @Test
    @DisplayName("게시글 삭제")
    void boardDelete(){
        //given
        UserEntity user1 = new UserEntity();
        user1.setUsername("user1");
        user1.setPassword("pw1");
        user1.setRole("ADMIN");

        Category category = Category.정보처리기사;
        BoardEntity board1 =
                BoardEntity
                        .builder()
                        .idx(1L)
                        .title("title 1")
                        .content("content 1")
                        .user(user1)
                        .category(category)
                        .build();

        userRepository.save(user1);
        boardRepository.save(board1);

        //when
        boardRepository.deleteById(1L);

        //then
        Boolean result = boardRepository.existsByIdx(1L);
        Assertions.assertThat(result).isEqualTo(false);
    }
}
