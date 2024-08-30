package com.shhwang0930.mytg.userexam.model;

import com.shhwang0930.mytg.exam.model.entity.ExamEntity;
import com.shhwang0930.mytg.user.model.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class UserExamEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userExamId;

    @ManyToOne
    @JoinColumn(name = "id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "jmcd")
    private ExamEntity exam;

    @Column
    private String date;

    @Builder
    public UserExamEntity(int userExamId, UserEntity user, ExamEntity exam,String date){
        this.userExamId = userExamId;
        this.user = user;
        this.exam = exam;
        this.date = date;
    }
}
