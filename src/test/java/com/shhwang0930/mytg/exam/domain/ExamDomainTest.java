package com.shhwang0930.mytg.exam.domain;

import com.shhwang0930.mytg.exam.model.entity.ExamEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ExamDomainTest {
    @Test
    @DisplayName("exam 생성 테스트")
    void createExam(){
        //given
        ExamEntity exam =
                ExamEntity
                        .builder()
                        .jmcd(123)
                        .jmfldnm("test jmfldnm")
                        .seriesnm("test seriesnm")
                        .build();

        //when, then
        Assertions.assertThat(exam.getJmcd()).isEqualTo(123);
        Assertions.assertThat(exam.getSeriesnm()).isEqualTo("test seriesnm");
        Assertions.assertThat(exam.getJmfldnm()).isEqualTo("test jmfldnm");
    }
}
