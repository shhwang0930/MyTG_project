package com.shhwang0930.mytg.exam.repository;

import com.shhwang0930.mytg.exam.model.ExamEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExamRepository extends JpaRepository<ExamEntity,Integer> {
    boolean existsBySeriesnm(String seriesnm);
}
