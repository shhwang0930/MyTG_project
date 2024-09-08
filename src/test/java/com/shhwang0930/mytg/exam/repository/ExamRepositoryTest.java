package com.shhwang0930.mytg.exam.repository;

import com.shhwang0930.mytg.config.TestConfig;
import com.shhwang0930.mytg.exam.model.entity.ExamEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(TestConfig.class)
public class ExamRepositoryTest {

    @Autowired
    ExamRepository examRepository;

    @Test
    @DisplayName("시험 생성")
    void examCreate(){
        //given
        ExamEntity exam =
                ExamEntity
                        .builder()
                        .seriesnm("test seriesnm")
                        .jmfldnm("test fmfldnm")
                        .jmcd(123)
                        .build();

        //when
        ExamEntity result = examRepository.save(exam);

        //then
        Assertions.assertThat(result.getJmfldnm()).isEqualTo(exam.getJmfldnm());
        Assertions.assertThat(result.getSeriesnm()).isEqualTo(exam.getSeriesnm());
        Assertions.assertThat(result.getJmcd()).isEqualTo(exam.getJmcd());
    }

    @Test
    @DisplayName("시험 종류로 시험 탐색")
    void findByJmfldnm(){
        String jm ="test";
        //given
        ExamEntity exam =
                ExamEntity
                        .builder()
                        .seriesnm("test seriesnm")
                        .jmfldnm(jm)
                        .jmcd(123)
                        .build();

        examRepository.save(exam);

        //when
        ExamEntity result = examRepository.findByJmfldnm(jm);

        //then
        Assertions.assertThat(result.getJmfldnm()).isEqualTo(exam.getJmfldnm());
        Assertions.assertThat(result.getSeriesnm()).isEqualTo(exam.getSeriesnm());
        Assertions.assertThat(result.getJmcd()).isEqualTo(exam.getJmcd());
    }

    @Test
    @DisplayName("종목 코드로 탐색")
    void findByJmcd(){
        int jmcd = 123;
        //given
        ExamEntity exam =
                ExamEntity
                        .builder()
                        .seriesnm("test seriesnm")
                        .jmfldnm("test jmfldnm")
                        .jmcd(jmcd)
                        .build();

        examRepository.save(exam);

        //when
        ExamEntity result = examRepository.findByJmcd(jmcd);

        //then
        Assertions.assertThat(result.getJmfldnm()).isEqualTo(exam.getJmfldnm());
        Assertions.assertThat(result.getSeriesnm()).isEqualTo(exam.getSeriesnm());
        Assertions.assertThat(result.getJmcd()).isEqualTo(exam.getJmcd());
    }
}
