package com.shhwang0930.mytg.comment.repository;

import com.shhwang0930.mytg.board.model.entity.BoardEntity;
import com.shhwang0930.mytg.board.model.entity.Category;
import com.shhwang0930.mytg.board.repository.BoardRepository;
import com.shhwang0930.mytg.comment.model.CommentEntity;
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
public class CommentRepositoryTest {
    @Autowired
    BoardRepository boardRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CommentRepository commentRepository;


    @Test
    @DisplayName("댓글 생성")
    void commentCreate(){
        //given
        UserEntity user = new UserEntity();
        user.setUsername("user1");
        user.setPassword("pw1");
        user.setRole("ADMIN");

        Category category = Category.정보처리기사;
        BoardEntity board =
                BoardEntity
                        .builder()
                        .title("title 1")
                        .content("content 1")
                        .user(user)
                        .category(category)
                        .build();

        CommentEntity comment =
                CommentEntity
                        .builder()
                        .commentDesc("test desc")
                        .user(user)
                        .board(board)
                        .build();

        //when
        userRepository.save(user);
        boardRepository.save(board);
        CommentEntity result = commentRepository.save(comment);

        //then
        Assertions.assertThat(result.getCommentDesc()).isEqualTo(comment.getCommentDesc());
        Assertions.assertThat(result.getBoard()).isEqualTo(comment.getBoard());
        Assertions.assertThat(result.getUser()).isEqualTo(comment.getUser());
    }

    @Test
    @DisplayName("댓글 리스트")
    void commentList(){
        //given
        UserEntity user = new UserEntity();
        user.setUsername("user1");
        user.setPassword("pw1");
        user.setRole("ADMIN");

        Category category = Category.정보처리기사;
        Long boardIdx = 1L;
        BoardEntity board =
                BoardEntity
                        .builder()
                        .idx(boardIdx)
                        .title("title 1")
                        .content("content 1")
                        .user(user)
                        .category(category)
                        .build();

        CommentEntity comment1 =
                CommentEntity
                        .builder()
                        .commentDesc("test desc1")
                        .user(user)
                        .board(board)
                        .build();

        CommentEntity comment2 =
                CommentEntity
                        .builder()
                        .commentDesc("test desc2")
                        .user(user)
                        .board(board)
                        .build();

        CommentEntity comment3 =
                CommentEntity
                        .builder()
                        .commentDesc("test desc3")
                        .user(user)
                        .board(board)
                        .build();

        userRepository.save(user);
        boardRepository.save(board);
        commentRepository.save(comment1);
        commentRepository.save(comment2);
        commentRepository.save(comment3);

        //when
        List<CommentEntity> result = commentRepository.findAllByBoardIdx(boardIdx);

        //then
        Assertions.assertThat(result.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("댓글 상세보기")
    void commentFind(){
        //given
        UserEntity user = new UserEntity();
        user.setUsername("user1");
        user.setPassword("pw1");
        user.setRole("ADMIN");

        Category category = Category.정보처리기사;
        BoardEntity board =
                BoardEntity
                        .builder()
                        .idx(1L)
                        .title("title 1")
                        .content("content 1")
                        .user(user)
                        .category(category)
                        .build();

        CommentEntity comment =
                CommentEntity
                        .builder()
                        .commentDesc("test desc")
                        .user(user)
                        .board(board)
                        .build();

        userRepository.save(user);
        boardRepository.save(board);
        commentRepository.save(comment);
        Long commentIdx = comment.getCommentIdx();

        //when
        CommentEntity result = commentRepository.findByCommentIdx(commentIdx);

        //then
        Assertions.assertThat(result).isEqualTo(comment);
    }

    @Test
    @DisplayName("댓글 존재유무")
    void commentIsExists(){
        //given
        UserEntity user = new UserEntity();
        user.setUsername("user1");
        user.setPassword("pw1");
        user.setRole("ADMIN");

        Category category = Category.정보처리기사;
        BoardEntity board =
                BoardEntity
                        .builder()
                        .idx(1L)
                        .title("title 1")
                        .content("content 1")
                        .user(user)
                        .category(category)
                        .build();

        CommentEntity comment =
                CommentEntity
                        .builder()
                        .commentDesc("test desc")
                        .user(user)
                        .board(board)
                        .build();

        userRepository.save(user);
        boardRepository.save(board);
        commentRepository.save(comment);

        //when
        Boolean result = commentRepository.existsByCommentIdx(comment.getCommentIdx());

        //then
        Assertions.assertThat(result).isEqualTo(true);
    }

    @Test
    @DisplayName("댓글 삭제")
    void commentDelete(){
        //given
        UserEntity user = new UserEntity();
        user.setUsername("user1");
        user.setPassword("pw1");
        user.setRole("ADMIN");

        Category category = Category.정보처리기사;
        BoardEntity board =
                BoardEntity
                        .builder()
                        .idx(1L)
                        .title("title 1")
                        .content("content 1")
                        .user(user)
                        .category(category)
                        .build();

        CommentEntity comment =
                CommentEntity
                        .builder()
                        .commentDesc("test desc")
                        .user(user)
                        .board(board)
                        .build();

        userRepository.save(user);
        boardRepository.save(board);
        commentRepository.save(comment);

        //when
        commentRepository.deleteById(comment.getCommentIdx());

        //then
        Boolean result = commentRepository.existsByCommentIdx(comment.getCommentIdx());
        Assertions.assertThat(result).isEqualTo(false);
    }
}
