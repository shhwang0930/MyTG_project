package com.shhwang0930.mytg.comment.service;

import com.shhwang0930.mytg.board.exception.BoardEntityNotFoundException;
import com.shhwang0930.mytg.board.model.dto.BoardDTO;
import com.shhwang0930.mytg.board.model.entity.BoardEntity;
import com.shhwang0930.mytg.board.model.entity.Category;
import com.shhwang0930.mytg.board.repository.BoardRepository;
import com.shhwang0930.mytg.comment.exception.CommentEntityNotFoundException;
import com.shhwang0930.mytg.comment.model.CommentDTO;
import com.shhwang0930.mytg.comment.model.CommentEntity;
import com.shhwang0930.mytg.comment.repository.CommentRepository;
import com.shhwang0930.mytg.user.model.entity.UserEntity;
import com.shhwang0930.mytg.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class CommentServiceTest {
    @Mock
    private BoardRepository boardRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentService commentService;

    private BoardEntity boardEntity;
    private CommentEntity commentEntity;
    private CommentEntity commentEntity2;
    private UserEntity loginedUser;
    private Long boardIdx = 1L;
    private Long commentIdx = 1L;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        UserEntity user = new UserEntity();
        user.setId(1);
        user.setUsername("test");
        user.setPassword("1234");
        user.setRole("ADMIN");

        // Sample BoardEntity
        boardEntity = BoardEntity.builder()
                .idx(boardIdx)
                .title("Sample Title")
                .content("Sample Content")
                .category(Category.가스기사)
                .user(user)
                .build();

        commentEntity = CommentEntity.builder()
                        .commentIdx(commentIdx)
                        .commentDesc("test desc")
                        .user(user)
                        .board(boardEntity)
                        .build();

        commentEntity2 = CommentEntity.builder()
                .commentDesc("test desc2")
                .user(user)
                .board(boardEntity)
                .build();

        this.loginedUser = new UserEntity();
        this.loginedUser.setId(2);
        this.loginedUser.setUsername("logined");
        this.loginedUser.setPassword("1234");
        this.loginedUser.setRole("ADMIN");

        // Mock the SecurityContextHolder
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("logined");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    @DisplayName("댓글 리스트")
    void readCommentList(){
        // given
        List<CommentEntity> commentEntities = Arrays.asList(commentEntity, commentEntity2);
        when(commentRepository.findAllByBoardIdx(boardIdx)).thenReturn(commentEntities);

        // when
        List<CommentDTO> result = commentService.readCommentList(boardIdx);

        // then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(commentEntities.size());
        assertThat(result).extracting("desc").containsExactly(commentEntity.getCommentDesc(), commentEntity2.getCommentDesc());
    }

    @Test
    @DisplayName("댓글 상세보기 성공")
    void readComment_Success(){
        // given
        when(commentRepository.findById(1L)).thenReturn(Optional.of(commentEntity));

        // when
        CommentDTO result = commentService.readComment(commentIdx);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getDesc()).isEqualTo(commentEntity.getCommentDesc());
        assertThat(result.getUser()).isEqualTo(commentEntity.getUser().getUsername());
        assertThat(result.getBoard()).isEqualTo(commentEntity.getBoard().getIdx());
    }

    @Test
    @DisplayName("댓글 상세보기 실패")
    void readComment_NotFound() {
        // given
        when(commentRepository.findById(commentIdx)).thenReturn(Optional.empty());

        // when & then
        assertThrows(CommentEntityNotFoundException.class, () -> commentService.readComment(commentIdx));
    }

    @Test
    @DisplayName("댓글 생성")
    void createComment(){
        // given
        CommentDTO comment = CommentDTO.builder()
                .desc("test desc")
                .board(boardIdx)
                .build();

        // Mock service methods
        when(userRepository.findByUsername("logined")).thenReturn(loginedUser);
        when(commentRepository.save(any(CommentEntity.class))).thenReturn(commentEntity);

        // when
        CommentDTO result = commentService.createComment(comment, boardIdx);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getDesc()).isEqualTo(commentEntity.getCommentDesc());
        assertThat(result.getUser()).isEqualTo(commentEntity.getUser().getUsername());
        assertThat(result.getBoard()).isEqualTo(commentEntity.getBoard().getIdx()); // Ensure category matches
    }

    @Test
    @DisplayName("댓글 수정 성공")
    void updateComment_SUCCESS(){
        // given
        CommentDTO comment = CommentDTO.builder()
                .desc("test desc")
                .board(boardIdx)
                .build();

        CommentEntity oldComment = CommentEntity.builder()
                .commentIdx(commentIdx)
                .commentDesc("Old Desc")
                .board(boardEntity)
                .user(loginedUser)
                .build();

        // Mock behaviors
        when(userRepository.findByUsername("logined")).thenReturn(loginedUser);
        when(boardRepository.findAllByIdx(boardIdx)).thenReturn(boardEntity);
        when(commentRepository.findById(commentIdx)).thenReturn(Optional.of(oldComment));
        when(commentRepository.save(any(CommentEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        CommentDTO newComment = commentService.updateComment(comment, commentIdx, boardIdx);

        // then
        verify(commentRepository, times(1)).findById(commentIdx);
        verify(commentRepository, times(1)).save(any(CommentEntity.class));

        assertEquals(comment.getDesc(), newComment.getDesc());
        assertEquals(comment.getBoard(), newComment.getBoard());
    }

    @Test
    @DisplayName("댓글 수정 실패 - 작성자가 아닌 경우")
    void updateComment_Unauthorized() {
        // given
        CommentDTO comment = CommentDTO.builder()
                .desc("test desc")
                .board(boardIdx)
                .build();

        UserEntity anotherUser = new UserEntity();
        anotherUser = new UserEntity();
        anotherUser.setId(3);
        anotherUser.setUsername("another");
        anotherUser.setPassword("1234");
        anotherUser.setRole("ADMIN");

        CommentEntity oldComment = CommentEntity.builder()
                .commentIdx(commentIdx)
                .commentDesc("Old Desc")
                .board(boardEntity)
                .user(anotherUser)
                .build();

        when(userRepository.findByUsername("logined")).thenReturn(loginedUser);
        when(commentRepository.findById(commentIdx)).thenReturn(Optional.of(oldComment));

        // when
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            commentService.updateComment(comment, commentIdx, boardIdx);
        });

        // then
        assertEquals("Unauthorized to update this comment", exception.getMessage());
        verify(commentRepository, times(1)).findById(commentIdx);
        verify(commentRepository, never()).save(any(CommentEntity.class));
    }

    @Test
    @DisplayName("댓글 수정 실패 - 게시물 없음")
    void updateComment_BoardNotFound() {
        // given
        CommentDTO comment = CommentDTO.builder()
                .desc("test desc")
                .board(boardIdx)
                .build();

        // Mock behavior
        when(userRepository.findByUsername("logined")).thenReturn(loginedUser);
        when(commentRepository.findById(commentIdx)).thenReturn(Optional.empty());

        // when
        assertThrows(CommentEntityNotFoundException.class, () -> {
            commentService.updateComment(comment, commentIdx, boardIdx);
        });

        // then
        verify(commentRepository, times(1)).findById(commentIdx);
        verify(commentRepository, never()).save(any(CommentEntity.class));
    }

    @Test
    @DisplayName("댓글 삭제 성공")
    void deleteComment(){
        // given
        CommentEntity commentDelete = CommentEntity.builder()
                .commentIdx(commentIdx)
                .commentDesc("delete Desc")
                .board(boardEntity)
                .user(loginedUser)
                .build();

        when(userRepository.findByUsername("logined")).thenReturn(loginedUser);
        when(commentRepository.findById(commentIdx)).thenReturn(Optional.of(commentDelete));

        // when
        commentService.deleteComment(commentIdx);

        // then
        verify(commentRepository, times(1)).findById(commentIdx);
        verify(commentRepository, times(1)).deleteByCommentIdx(commentIdx);
    }
    @Test
    @DisplayName("댓글 삭제 실패 - 작성자가 아닌 경우")
    void deleteComment_Unauthorized() {
        // Given
        UserEntity anotherUser = new UserEntity();
        anotherUser = new UserEntity();
        anotherUser.setId(3);
        anotherUser.setUsername("another");
        anotherUser.setPassword("1234");
        anotherUser.setRole("ADMIN");

        CommentEntity commentDelete = CommentEntity.builder()
                .commentIdx(commentIdx)
                .commentDesc("delete Desc")
                .board(boardEntity)
                .user(anotherUser)
                .build();

        // Mock behavior
        when(userRepository.findByUsername("logined")).thenReturn(loginedUser);
        when(commentRepository.findById(commentIdx)).thenReturn(Optional.of(commentDelete));

        // When
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            commentService.deleteComment(commentIdx);
        });

        // Then
        assertEquals("Unauthorized to update this comment", exception.getMessage());
        verify(commentRepository, times(1)).findById(commentIdx);
        verify(commentRepository, never()).deleteByCommentIdx(anyLong());
    }

    @Test
    @DisplayName("댓글 삭제 - 댓글이 존재하지 않음")
    void deleteComment_NotFound() {
        // Given
        // Mock behavior: 게시물이 존재하지 않는 경우
        when(commentRepository.findById(commentIdx)).thenReturn(Optional.empty());

        // When
        assertThrows(CommentEntityNotFoundException.class, () -> {
            commentService.deleteComment(commentIdx);
        });

        // Then
        verify(commentRepository, times(1)).findById(commentIdx);
        verify(commentRepository, never()).deleteByCommentIdx(anyLong()); // 삭제가 발생하지 않아야 함
    }
}
