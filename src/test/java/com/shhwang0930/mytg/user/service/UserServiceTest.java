package com.shhwang0930.mytg.user.service;

import com.shhwang0930.mytg.user.model.dto.JoinDTO;
import com.shhwang0930.mytg.user.model.dto.PasswordDTO;
import com.shhwang0930.mytg.user.model.entity.UserEntity;
import com.shhwang0930.mytg.user.repository.UserRepository;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @InjectMocks
    private UserService userService;

    private static final char[] rndAllCharacters = new char[]{
            //number
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            //uppercase
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            //lowercase
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            //special symbols
            '@', '$', '!', '%', '*', '?', '&'
    };

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Mock 객체 초기화
    }


    @Test
    @DisplayName("회원 가입")
    void join(){
        // given
        JoinDTO joinDTO = new JoinDTO();
        joinDTO.setUsername("testUser");
        joinDTO.setPassword("testPassword");

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("testUser");
        userEntity.setPassword("encryptedPassword");
        userEntity.setRole("ROLE_ADMIN");

        when(bCryptPasswordEncoder.encode("testPassword")).thenReturn("encryptedPassword");

        // when
        String result = userService.joinUser(joinDTO);

        // then
        assertEquals("success", result);
        verify(userRepository, times(1)).save(any(UserEntity.class)); // userRepository.save가 1번 호출되었는지 확인
        verify(bCryptPasswordEncoder, times(1)).encode("testPassword"); // bCryptPasswordEncoder.encode가 1번 호출되었는지 확인
    }

    @Test
    @DisplayName("비밀번호 생성")
    public void generatePassword() {
        // given
        String username = "testUser";
        UserEntity mockUser = new UserEntity();
        mockUser.setUsername(username);

        // findByUsername에 대한 Mock 설정
        when(userRepository.findByUsername(username)).thenReturn(mockUser);

        // 비밀번호가 암호화되는 부분 설정
        String plainPassword = "randomPassword"; // 랜덤 비밀번호로 가정
        String encryptedPassword = "encryptedPassword"; // 암호화된 비밀번호로 가정
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn(encryptedPassword);

        // when
        PasswordDTO result = userService.generatePassword(username);

        // then
        assertNotNull(result); // 결과가 null이 아님을 확인
        assertEquals(10, result.getPassword().length()); // 생성된 비밀번호가 10글자인지 확인

        // Mock 객체가 호출되었는지 검증
        verify(userRepository, times(1)).findByUsername(username); // findByUsername이 1번 호출되었는지 검증
        verify(bCryptPasswordEncoder, times(1)).encode(anyString()); // bCryptPasswordEncoder.encode가 1번 호출되었는지 검증
        verify(userRepository, times(1)).save(mockUser); // userRepository.save가 1번 호출되었는지 검증

        // 비밀번호가 mockUser 객체에 제대로 설정되었는지 확인
        assertEquals(encryptedPassword, mockUser.getPassword());
    }

    @Test
    @DisplayName("비밀번호 수정")
    void pwUpdate(){
        // given
        String username = "testUser";
        String plainPassword = "newPassword";
        String encryptedPassword = "encryptedNewPassword";

        // 가짜 UserEntity 생성
        UserEntity mockUser = new UserEntity();
        mockUser.setUsername(username);

        // findByUsername에 대한 Mock 설정
        when(userRepository.findByUsername(username)).thenReturn(mockUser);

        // 비밀번호 암호화 부분 설정
        when(bCryptPasswordEncoder.encode(plainPassword)).thenReturn(encryptedPassword);

        // when
        userService.updatePassword(username, plainPassword);

        // then
        // mockUser의 비밀번호가 암호화된 값으로 설정되었는지 확인
        assertEquals(encryptedPassword, mockUser.getPassword());

        // Mock 객체가 제대로 호출되었는지 검증
        verify(userRepository, times(1)).findByUsername(username); // findByUsername이 1번 호출되었는지 검증
        verify(bCryptPasswordEncoder, times(1)).encode(plainPassword); // encode 메서드가 호출되었는지 검증
        verify(userRepository, times(1)).save(mockUser); // save가 1번 호출되었는지 검증
    }

}
