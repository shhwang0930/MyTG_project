package com.shhwang0930.mytg.userexam.model;

import com.shhwang0930.mytg.exam.model.entity.ExamEntity;
import com.shhwang0930.mytg.user.model.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserExamDTO {
    private int userExamId;
    private String username;
    private int jmcd;
    private String examDate;

    public static UserExamDTO fromEntity(UserExamEntity userExam){
        return UserExamDTO.builder()
                .userExamId(userExam.getUserExamId())
                .username(userExam.getUser().getUsername())
                .jmcd(userExam.getExam().getJmcd())
                .examDate(userExam.getDate())
                .build();
    }

    public static List<UserExamDTO> fromEntityList(List<UserExamEntity> userExamList){
        return userExamList.stream()
                .map(UserExamDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public UserExamEntity toEntity(UserEntity user, ExamEntity exam) {
        return UserExamEntity.builder()
                .userExamId(this.userExamId)
                .user(user)
                .exam(exam)
                .date(this.examDate)
                .build();
    }
}
