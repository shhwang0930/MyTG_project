package com.shhwang0930.mytg.board.service;

import com.shhwang0930.mytg.board.exception.BoardEntityNotFoundException;
import com.shhwang0930.mytg.board.model.dto.BoardDTO;
import com.shhwang0930.mytg.board.model.entity.BoardEntity;
import com.shhwang0930.mytg.board.model.entity.Category;
import com.shhwang0930.mytg.board.repository.BoardRepository;
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


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

public class BoardServiceTest {
    @Mock
    private BoardRepository boardRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BoardService boardService;

    private BoardEntity boardEntity;
    private BoardEntity boardEntity2;
    private UserEntity loginedUser;
    private BoardDTO board;

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
                .idx(1L)
                .title("Sample Title")
                .content("Sample Content")
                .category(Category.가스기사)
                .user(user)
                .build();

        boardEntity2 = BoardEntity.builder()
                .idx(2L)
                .title("Sample Title2")
                .content("Sample Content2")
                .category(Category.가스기사)
                .user(user)
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
    @DisplayName("게시글 생성")
    void createBoard(){
        // given

        board = BoardDTO.builder()
                .title("Sample Title")
                .content("Sample Content")
                .category(Category.가스기사.name()) // Ensure valid category
                .build();

        // Mock service methods
        when(userRepository.findByUsername("logined")).thenReturn(loginedUser);
        when(boardRepository.save(any(BoardEntity.class))).thenReturn(boardEntity);

        // when
        BoardDTO result = boardService.createBoard(board);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo(boardEntity.getTitle());
        assertThat(result.getContent()).isEqualTo(boardEntity.getContent());
        assertThat(result.getCategory()).isEqualTo(boardEntity.getCategory().name()); // Ensure category matches
    }

    @Test
    @DisplayName("게시글 리스트")
    void readBoardList(){
        // given
        List<BoardEntity> boardEntities = Arrays.asList(boardEntity, boardEntity2);
        when(boardRepository.findAll()).thenReturn(boardEntities);

        // when
        List<BoardDTO> result = boardService.readBoardList();

        // then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(boardEntities.size());
        assertThat(result).extracting("title").containsExactly(boardEntity.getTitle(), boardEntity2.getTitle());
        assertThat(result).extracting("content").containsExactly(boardEntity.getContent(), boardEntity2.getContent());
    }

    @Test
    @DisplayName("게시글 상세보기 성공")
    void readBoard_Success(){
        // given
        when(boardRepository.findById(1L)).thenReturn(Optional.of(boardEntity));

        // when
        BoardDTO result = boardService.readBoard(1L);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo(boardEntity.getTitle());
        assertThat(result.getContent()).isEqualTo(boardEntity.getContent());
    }
    @Test
    @DisplayName("게시글 상세보기 실패")
    void readBoard_NotFound() {
        // given
        when(boardRepository.findById(1L)).thenReturn(Optional.empty());

        // when & then
        assertThrows(BoardEntityNotFoundException.class, () -> boardService.readBoard(1L));
    }

    @Test
    @DisplayName("게시글 수정 성공")
    void updateBoard_SUCCESS(){
        // given
        Long boardIdx = 1L; // Assuming boardIdx should match an existing entity's ID
        BoardDTO boardDTO = BoardDTO.builder()
                .title("New Title")
                .content("New Content")
                .category(Category.가스기사.name()) // Replace with an actual category
                .build();

        BoardEntity oldBoard = BoardEntity.builder()
                .idx(boardIdx)
                .title("Old Title")
                .content("Old Content")
                .category(Category.가스기사)
                .user(loginedUser)
                .build();

        // Mock behaviors
        when(userRepository.findByUsername("logined")).thenReturn(loginedUser);
        when(boardRepository.findById(boardIdx)).thenReturn(Optional.of(oldBoard));
        when(boardRepository.save(any(BoardEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        BoardDTO newBoard = boardService.updateBoard(boardIdx, boardDTO);

        // then
        verify(boardRepository, times(1)).findById(boardIdx);
        verify(boardRepository, times(1)).save(any(BoardEntity.class));

        assertEquals(boardDTO.getTitle(), newBoard.getTitle());
        assertEquals(boardDTO.getContent(), newBoard.getContent());
        assertEquals(boardDTO.getCategory(), newBoard.getCategory());
    }

    @Test
    @DisplayName("게시글 수정 실패 - 작성자가 아닌 경우")
    void updateBoard_Unauthorized() {
        // given
        Long boardIdx = 1L;
        BoardDTO boardDTO = BoardDTO.builder()
                .title("New Title")
                .content("New Content")
                .category(Category.가스기사.name())
                .build();

        UserEntity anotherUser = new UserEntity();
        anotherUser = new UserEntity();
        anotherUser.setId(3);
        anotherUser.setUsername("another");
        anotherUser.setPassword("1234");
        anotherUser.setRole("ADMIN");

        BoardEntity oldBoard = BoardEntity.builder()
                .idx(boardIdx)
                .title("Old Title")
                .content("Old Content")
                .category(Category.가스기사)
                .user(anotherUser)
                .build();

        when(userRepository.findByUsername("logined")).thenReturn(loginedUser);
        when(boardRepository.findById(boardIdx)).thenReturn(Optional.of(oldBoard));

        // when
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            boardService.updateBoard(boardIdx, boardDTO);
        });

        // then
        assertEquals("Unauthorized to update this board", exception.getMessage());
        verify(boardRepository, times(1)).findById(boardIdx);
        verify(boardRepository, never()).save(any(BoardEntity.class));
    }

    @Test
    @DisplayName("게시글 수정 실패 - 게시물 없음")
    void updateBoard_BoardNotFound() {
        // given
        Long boardIdx = 1L;
        BoardDTO boardDTO = BoardDTO.builder()
                .title("New Title")
                .content("New Content")
                .category(Category.가스기사.name())
                .build();

        // Mock behavior
        when(userRepository.findByUsername("logined")).thenReturn(loginedUser);
        when(boardRepository.findById(boardIdx)).thenReturn(Optional.empty());

        // when
        assertThrows(BoardEntityNotFoundException.class, () -> {
            boardService.updateBoard(boardIdx, boardDTO);
        });

        // then
        verify(boardRepository, times(1)).findById(boardIdx);
        verify(boardRepository, never()).save(any(BoardEntity.class));
    }

    @Test
    @DisplayName("게시글 삭제 성공")
    void deleteBoard(){
        // given
        Long boardIdx = 1L;

        BoardEntity boardDelete = BoardEntity.builder()
                .idx(boardIdx)
                .title("delete Title")
                .content("delete Content")
                .category(Category.가스기사)
                .user(loginedUser)
                .build();

        when(userRepository.findByUsername("logined")).thenReturn(loginedUser);
        when(boardRepository.findById(boardIdx)).thenReturn(Optional.of(boardDelete));

        // when
        boardService.deleteBoard(boardIdx);

        // then
        verify(boardRepository, times(1)).findById(boardIdx);
        verify(boardRepository, times(1)).deleteByIdx(boardIdx);
    }

    @Test
    @DisplayName("게시글 삭제 실패 - 작성자가 아닌 경우")
    void deleteBoard_Unauthorized() {
        // Given
        Long boardIdx = 1L;

        UserEntity anotherUser = new UserEntity();
        anotherUser = new UserEntity();
        anotherUser.setId(3);
        anotherUser.setUsername("another");
        anotherUser.setPassword("1234");
        anotherUser.setRole("ADMIN");

        BoardEntity boardDelete = BoardEntity.builder()
                .idx(boardIdx)
                .title("delete Title")
                .content("delete Content")
                .category(Category.가스기사)
                .user(anotherUser)
                .build();

        // Mock behavior
        when(userRepository.findByUsername("logined")).thenReturn(loginedUser);
        when(boardRepository.findById(boardIdx)).thenReturn(Optional.of(boardDelete));

        // When
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            boardService.deleteBoard(boardIdx);
        });

        // Then
        assertEquals("Unauthorized to update this board", exception.getMessage());
        verify(boardRepository, times(1)).findById(boardIdx);
        verify(boardRepository, never()).deleteByIdx(anyLong());
    }

    @Test
    @DisplayName("게시글 삭제 - 게시물이 존재하지 않음")
    void deleteBoard_NotFound() {
        // Given
        Long boardIdx = 1L;

        // Mock behavior: 게시물이 존재하지 않는 경우
        when(boardRepository.findById(boardIdx)).thenReturn(Optional.empty());

        // When
        assertThrows(BoardEntityNotFoundException.class, () -> {
            boardService.deleteBoard(boardIdx);
        });

        // Then
        verify(boardRepository, times(1)).findById(boardIdx);
        verify(boardRepository, never()).deleteByIdx(anyLong()); // 삭제가 발생하지 않아야 함
    }
}
