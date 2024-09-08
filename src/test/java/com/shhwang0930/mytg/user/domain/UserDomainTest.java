package com.shhwang0930.mytg.user.domain;

import com.shhwang0930.mytg.exam.model.entity.ExamEntity;
import com.shhwang0930.mytg.user.model.entity.UserEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserDomainTest {
    @Test
    @DisplayName("exam 생성 테스트")
    void createUser(){
        //given
        UserEntity user = new UserEntity();
        user.setUsername("test");
        user.setPassword("1234");
        user.setRole("ADMIN");

        //when, then
        Assertions.assertThat(user.getUsername()).isEqualTo("test");
        Assertions.assertThat(user.getPassword()).isEqualTo("1234");
        Assertions.assertThat(user.getRole()).isEqualTo("ADMIN");
    }
}
