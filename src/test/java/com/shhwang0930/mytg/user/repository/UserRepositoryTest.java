package com.shhwang0930.mytg.user.repository;

import com.shhwang0930.mytg.config.TestConfig;
import com.shhwang0930.mytg.user.model.entity.UserEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(TestConfig.class)
public class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("사용자 생성")
    void userCreate(){
        //given
        UserEntity user = new UserEntity();
        user.setUsername("test");
        user.setPassword("1234");
        user.setRole("ADMIN");

        //when
        UserEntity result = userRepository.save(user);

        //then
        Assertions.assertThat(result.getUsername()).isEqualTo(user.getUsername());
        Assertions.assertThat(result.getRole()).isEqualTo(user.getRole());
        Assertions.assertThat(result.getPassword()).isEqualTo(user.getPassword());
    }

    @Test
    @DisplayName("사용자 탐색")
    void findByUsername(){
        //given
        String username = "test";

        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword("1234");
        user.setRole("ADMIN");
        userRepository.save(user);

        //when
        UserEntity result = userRepository.findByUsername(username);

        //then
        Assertions.assertThat(result.getUsername()).isEqualTo(user.getUsername());
        Assertions.assertThat(result.getRole()).isEqualTo(user.getRole());
        Assertions.assertThat(result.getPassword()).isEqualTo(user.getPassword());
    }

    @Test
    @DisplayName("사용자 존재 유무 확인")
    void existsByUsername(){
        //given
        String username = "test";

        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword("1234");
        user.setRole("ADMIN");
        userRepository.save(user);

        //when
        Boolean result = userRepository.existsByUsername(username);

        //then
        Assertions.assertThat(result).isEqualTo(true);
    }
}
