package com.shhwang0930.mytg.userexam.service;


import com.shhwang0930.mytg.board.exception.BoardEntityNotFoundException;
import com.shhwang0930.mytg.board.model.entity.BoardEntity;
import com.shhwang0930.mytg.board.model.entity.Category;
import com.shhwang0930.mytg.exam.model.entity.ExamEntity;
import com.shhwang0930.mytg.exam.repository.ExamRepository;
import com.shhwang0930.mytg.user.model.entity.UserEntity;
import com.shhwang0930.mytg.user.repository.UserRepository;
import com.shhwang0930.mytg.userexam.exception.UserExamEntityNotFoundException;
import com.shhwang0930.mytg.userexam.model.UserExamDTO;
import com.shhwang0930.mytg.userexam.model.UserExamEntity;
import com.shhwang0930.mytg.userexam.repository.UserExamRepository;
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

public class UserExamServiceTest {
    @Mock
    UserRepository userRepository;
    @Mock
    ExamRepository examRepository;
    @Mock
    UserExamRepository userExamRepository;
    @InjectMocks
    UserExamService userExamService;


    private UserEntity loginedUser;
    private ExamEntity exam;
    private ExamEntity exam2;
    private UserExamEntity userExam;
    private UserExamEntity userExam2;
    private int userId = 1;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        UserEntity user = new UserEntity();
        user.setId(2);
        user.setUsername("test");
        user.setPassword("1234");
        user.setRole("ADMIN");

        exam = ExamEntity.builder()
                .jmcd(123)
                .seriesnm("test seriesnm")
                .jmfldnm("test jmfldnm")
                .build();

        exam2 = ExamEntity.builder()
                .jmcd(111)
                .seriesnm("test seriesnm2")
                .jmfldnm("test jmfldnm2")
                .build();

        // Sample UserExamEntity
        userExam = UserExamEntity.builder()
                .user(user)
                .exam(exam)
                .date("20240908")
                .build();

        userExam2 = UserExamEntity.builder()
                .user(user)
                .exam(exam)
                .date("20240907")
                .build();

        this.loginedUser = new UserEntity();
        this.loginedUser.setId(userId);
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
    @DisplayName("userexam 리스트")
    void readUserExamList(){
        // given
        List<UserExamEntity> userExamEntities = Arrays.asList(userExam, userExam2);
        when(userRepository.findByUsername("logined")).thenReturn(loginedUser);
        when(userExamRepository.findByUserId(userId)).thenReturn(userExamEntities);

        // when
        List<UserExamDTO> result = userExamService.readUserExamList();

        // then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(userExamEntities.size());
        assertThat(result).extracting("examDate").containsExactly(userExam.getDate(), userExam2.getDate());
    }

    @Test
    @DisplayName("userexam 상세보기")
    void readUserExam(){
        // given
        int userExamId = userExam.getUserExamId();
        when(userRepository.findByUsername("logined")).thenReturn(loginedUser);
        when(userExamRepository.findByUserExamId(userExamId)).thenReturn(userExam);

        // when
        UserExamDTO result = userExamService.readUserExam(userExamId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getExamDate()).isEqualTo(userExam.getDate());
        assertThat(result.getJmcd()).isEqualTo(userExam.getExam().getJmcd());
        assertThat(result.getUsername()).isEqualTo(userExam.getUser().getUsername());
    }

    @Test
    @DisplayName("userexam 생성")
    void createUserExam(){
        // given
        UserExamDTO userExamDto = UserExamDTO.builder()
                .examDate("20240908")
                .jmcd(123)
                .build();

        // Mock service methods
        when(userRepository.findByUsername("logined")).thenReturn(loginedUser);
        when(examRepository.findByJmcd(123)).thenReturn(exam);
        when(userExamRepository.save(any(UserExamEntity.class))).thenReturn(userExam);

        // when
        UserExamDTO result = userExamService.createUserExam(userExamDto);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getExamDate()).isEqualTo(userExam.getDate());
        assertThat(result.getUsername()).isEqualTo(userExam.getUser().getUsername());
        assertThat(result.getJmcd()).isEqualTo(userExam.getExam().getJmcd());
    }

    @Test
    @DisplayName("userexam 수정 성공")
    void userExamUpdate_Success(){
        // given
        int userExamId = 1;
        UserExamDTO userExamDTO = UserExamDTO.builder()
                .examDate("20240908")
                .jmcd(111)
                .build();

        UserExamEntity oldUserExam = UserExamEntity.builder()
                .userExamId(userExamId)
                .exam(exam)
                .date("20240907")
                .user(loginedUser)
                .build();

        // Mock behaviors
        when(userRepository.findByUsername("logined")).thenReturn(loginedUser);
        when(examRepository.findByJmcd(111)).thenReturn(exam2);
        when(userExamRepository.findById(userExamId)).thenReturn(Optional.of(oldUserExam));
        when(userExamRepository.save(any(UserExamEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        UserExamDTO newUserExam = userExamService.updateUserExam(userExamDTO, userExamId);

        // then
        verify(userExamRepository, times(1)).findById(userExamId);
        verify(userExamRepository, times(1)).save(any(UserExamEntity.class));

        assertEquals(userExamDTO.getExamDate(), newUserExam.getExamDate());
        assertEquals(userExamDTO.getJmcd(), newUserExam.getJmcd());
    }

    @Test
    @DisplayName("userexam 수정 실패 - 작성자가 아닌 경우")
    void updateUserExam_Unauthorized(){
        // given
        int userExamId = 1;
        UserExamDTO userExamDTO = UserExamDTO.builder()
                .examDate("20240908")
                .jmcd(111)
                .build();

        UserEntity anotherUser = new UserEntity();
        anotherUser = new UserEntity();
        anotherUser.setId(3);
        anotherUser.setUsername("another");
        anotherUser.setPassword("1234");
        anotherUser.setRole("ADMIN");

        UserExamEntity oldUserExam = UserExamEntity.builder()
                .userExamId(userExamId)
                .exam(exam)
                .date("20240907")
                .user(anotherUser)
                .build();

        when(userRepository.findByUsername("logined")).thenReturn(loginedUser);
        when(userExamRepository.findById(userExamId)).thenReturn(Optional.of(oldUserExam));

        // when
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userExamService.updateUserExam(userExamDTO, userExamId);
        });

        // then
        assertEquals("Unauthorized to update this userExam", exception.getMessage());
        verify(userExamRepository, times(1)).findById(userExamId);
        verify(userExamRepository, never()).save(any(UserExamEntity.class));
    }

    @Test
    @DisplayName("userexam 수정 실패 - userexam 없음")
    void updateUserExam_UserExamNotFound(){
        // given
        int userExamId = 1;
        UserExamDTO userExamDTO = UserExamDTO.builder()
                .examDate("20240908")
                .jmcd(111)
                .build();

        // Mock behavior
        when(userRepository.findByUsername("logined")).thenReturn(loginedUser);
        when(userExamRepository.findById(userExamId)).thenReturn(Optional.empty());

        // when
        assertThrows(UserExamEntityNotFoundException.class, () -> {
            userExamService.updateUserExam(userExamDTO, userExamId);
        });

        // then
        verify(userExamRepository, times(1)).findById(userExamId);
        verify(userExamRepository, never()).save(any(UserExamEntity.class));
    }

    @Test
    @DisplayName("userexam 삭제 성공")
    void userExamDelete_Success(){
        // given
        int userExamId = 1;

        UserExamEntity userExamDelete = UserExamEntity.builder()
                .userExamId(userExamId)
                .exam(exam)
                .date("20240907")
                .user(loginedUser)
                .build();

        when(userRepository.findByUsername("logined")).thenReturn(loginedUser);
        when(userExamRepository.findById(userExamId)).thenReturn(Optional.of(userExamDelete));

        // when
        userExamService.deleteUserExam(userExamId);

        // then
        verify(userExamRepository, times(1)).findById(userExamId);
        verify(userExamRepository, times(1)).deleteByUserExamId(userExamId);
    }

    @Test
    @DisplayName("userexam 삭제 실패 - 작성자가 아닌 경우")
    void deleteUserExam_Unauthorized(){
        // given
        int userExamId = 1;

        UserEntity anotherUser = new UserEntity();
        anotherUser = new UserEntity();
        anotherUser.setId(3);
        anotherUser.setUsername("another");
        anotherUser.setPassword("1234");
        anotherUser.setRole("ADMIN");

        UserExamEntity userExamDelete = UserExamEntity.builder()
                .userExamId(userExamId)
                .exam(exam)
                .date("20240907")
                .user(anotherUser)
                .build();

        // Mock behavior
        when(userRepository.findByUsername("logined")).thenReturn(loginedUser);
        when(userExamRepository.findById(userExamId)).thenReturn(Optional.of(userExamDelete));

        // when
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userExamService.deleteUserExam(userExamId);
        });

        // then
        assertEquals("Unauthorized to update this userExam", exception.getMessage());
        verify(userExamRepository, times(1)).findById(userExamId);
        verify(userExamRepository, never()).deleteByUserExamId(anyInt());
    }

    @Test
    @DisplayName("userexam 삭제 실패 - userexam 없음")
    void deleteUserExam_NotFound(){
        // Given
        int userExamId = 1;
        when(userExamRepository.findById(userExamId)).thenReturn(Optional.empty());

        // When
        assertThrows(UserExamEntityNotFoundException.class, () -> {
            userExamService.deleteUserExam(userExamId);
        });

        // Then
        verify(userExamRepository, times(1)).findById(userExamId);
        verify(userExamRepository, never()).deleteByUserExamId(anyInt()); // 삭제가 발생하지 않아야 함
    }
}
