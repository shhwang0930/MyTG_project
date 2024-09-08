package com.shhwang0930.mytg.userexam.respository;

import com.shhwang0930.mytg.board.repository.BoardRepository;
import com.shhwang0930.mytg.comment.model.CommentEntity;
import com.shhwang0930.mytg.comment.repository.CommentRepository;
import com.shhwang0930.mytg.config.TestConfig;
import com.shhwang0930.mytg.exam.model.entity.ExamEntity;
import com.shhwang0930.mytg.exam.repository.ExamRepository;
import com.shhwang0930.mytg.user.model.entity.UserEntity;
import com.shhwang0930.mytg.user.repository.UserRepository;
import com.shhwang0930.mytg.userexam.model.UserExamEntity;
import com.shhwang0930.mytg.userexam.repository.UserExamRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

@DataJpaTest
@Import(TestConfig.class)
public class UserExamRepositoryTest {

    @Autowired
    ExamRepository examRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserExamRepository userExamRepository;

    @Test
    @DisplayName("userexam 생성")
    void userExamCreate(){
        //given
        UserEntity user = new UserEntity();
        user.setUsername("user");
        user.setPassword("pw");
        user.setRole("ADMIN");

        ExamEntity exam =
                ExamEntity
                        .builder()
                        .seriesnm("test seriesnm")
                        .jmfldnm("test fmfldnm")
                        .jmcd(123)
                        .build();

        UserExamEntity userExam =
                UserExamEntity
                        .builder()
                        .user(user)
                        .exam(exam)
                        .date("20240908")
                        .build();

        //when
        userRepository.save(user);
        examRepository.save(exam);
        UserExamEntity result = userExamRepository.save(userExam);

        //then
        Assertions.assertThat(result.getExam()).isEqualTo(userExam.getExam());
        Assertions.assertThat(result.getDate()).isEqualTo(userExam.getDate());
        Assertions.assertThat(result.getUser()).isEqualTo(userExam.getUser());
        // 만약 userExamId 필드가 엔티티에서 @GeneratedValue로 설정되어 있다면, 기본 키는 데이터베이스에서 자동으로 생성됨 이 경우, 수동으로 userExamId를 설정하려고 하면 ORM이 충돌을 일으켜 오류가 발생할 수 있다.
    }

    @Test
    @DisplayName("userexam 리스트")
    void userExamList(){
        //given
        int userId = 1;

        UserEntity user = new UserEntity();
        user.setId(userId);
        user.setUsername("user");
        user.setPassword("pw");
        user.setRole("ADMIN");

        ExamEntity exam =
                ExamEntity
                        .builder()
                        .seriesnm("test seriesnm")
                        .jmfldnm("test fmfldnm")
                        .jmcd(123)
                        .build();

        UserExamEntity userExam1 =
                UserExamEntity
                        .builder()
                        .user(user)
                        .exam(exam)
                        .date("20240908")
                        .build();
        UserExamEntity userExam2 =
                UserExamEntity
                        .builder()
                        .user(user)
                        .exam(exam)
                        .date("20240907")
                        .build();
        UserExamEntity userExam3 =
                UserExamEntity
                        .builder()
                        .user(user)
                        .exam(exam)
                        .date("20240906")
                        .build();

        //when
        userRepository.save(user);
        examRepository.save(exam);
        userExamRepository.save(userExam1);
        userExamRepository.save(userExam2);
        userExamRepository.save(userExam3);
        List<UserExamEntity> result = userExamRepository.findByUserId(userId);

        //then
        Assertions.assertThat(result.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("userexam 상세보기")
    void findByUserExamId(){
        //given
        UserEntity user = new UserEntity();
        user.setUsername("user");
        user.setPassword("pw");
        user.setRole("ADMIN");

        ExamEntity exam =
                ExamEntity
                        .builder()
                        .seriesnm("test seriesnm")
                        .jmfldnm("test fmfldnm")
                        .jmcd(123)
                        .build();

        UserExamEntity userExam =
                UserExamEntity
                        .builder()
                        .user(user)
                        .exam(exam)
                        .date("20240908")
                        .build();

        //when
        userRepository.save(user);
        examRepository.save(exam);
        userExamRepository.save(userExam);
        int ueId = userExam.getUserExamId();

        UserExamEntity result = userExamRepository.findByUserExamId(ueId);

        //then
        Assertions.assertThat(result).isEqualTo(userExam);
    }

    @Test
    @DisplayName("userexam 존재 유무")
    void existsByUserExamId(){
        //given
        UserEntity user = new UserEntity();
        user.setUsername("user");
        user.setPassword("pw");
        user.setRole("ADMIN");

        ExamEntity exam =
                ExamEntity
                        .builder()
                        .seriesnm("test seriesnm")
                        .jmfldnm("test fmfldnm")
                        .jmcd(123)
                        .build();

        UserExamEntity userExam =
                UserExamEntity
                        .builder()
                        .user(user)
                        .exam(exam)
                        .date("20240908")
                        .build();
        //when
        userRepository.save(user);
        examRepository.save(exam);
        userExamRepository.save(userExam);
        int ueId = userExam.getUserExamId();

        Boolean result = userExamRepository.existsByUserExamId(ueId);

        //then
        Assertions.assertThat(result).isEqualTo(true);
    }
    @Test
    @DisplayName("userexam 삭제")
    void deleteByUserExamId(){
        //given
        UserEntity user = new UserEntity();
        user.setUsername("user");
        user.setPassword("pw");
        user.setRole("ADMIN");

        ExamEntity exam =
                ExamEntity
                        .builder()
                        .seriesnm("test seriesnm")
                        .jmfldnm("test fmfldnm")
                        .jmcd(123)
                        .build();

        UserExamEntity userExam =
                UserExamEntity
                        .builder()
                        .user(user)
                        .exam(exam)
                        .date("20240908")
                        .build();

        //when
        userRepository.save(user);
        examRepository.save(exam);
        userExamRepository.save(userExam);
        int ueId = userExam.getUserExamId();
        userExamRepository.deleteByUserExamId(ueId);

        //then
        Boolean result = userExamRepository.existsByUserExamId(userExam.getUserExamId());
        Assertions.assertThat(result).isEqualTo(false);
    }
}
