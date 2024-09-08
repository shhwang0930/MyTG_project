package com.shhwang0930.mytg.userexam.domain;

import com.shhwang0930.mytg.exam.model.entity.ExamEntity;
import com.shhwang0930.mytg.user.model.entity.UserEntity;
import com.shhwang0930.mytg.userexam.model.UserExamEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserExamDomainTest {
    @Test
    @DisplayName("userexam 생성 테스트")
    void createUserExam(){
        //given
        UserEntity user = new UserEntity();
        user.setUsername("test");
        user.setPassword("1234");
        user.setRole("ADMIN");

        ExamEntity exam =
                ExamEntity
                        .builder()
                        .jmcd(123)
                        .jmfldnm("test jmfldnm")
                        .seriesnm("test seriesnm")
                        .build();

        UserExamEntity userExam =
                UserExamEntity
                        .builder()
                        .userExamId(1)
                        .user(user)
                        .exam(exam)
                        .date("20240908")
                        .build();

        //when, then
        Assertions.assertThat(userExam.getUserExamId()).isEqualTo(1);
        Assertions.assertThat(userExam.getUser()).isEqualTo(user);
        Assertions.assertThat(userExam.getExam()).isEqualTo(exam);
        Assertions.assertThat(userExam.getDate()).isEqualTo("20240908");
    }
}
