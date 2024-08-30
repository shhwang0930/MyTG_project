package com.shhwang0930.mytg.userexam.repository;

import com.shhwang0930.mytg.userexam.model.UserExamEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserExamRepository extends JpaRepository<UserExamEntity, Integer> {
    List<UserExamEntity> findByUserId(int id);
    UserExamEntity findByUserExamId(int userExamId);
    void deleteByUserExamId(int userExamId);
    boolean existsByUserExamId(int userExamId);
}
