package com.shhwang0930.mytg.comment.repository;

import com.shhwang0930.mytg.comment.model.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    List<CommentEntity> findAllByBoardIdx(Long idx);
    CommentEntity findByCommentIdx(Long idx);
    void deleteByCommentIdx(Long idx);
    boolean existsByCommentIdx(Long commentIdx);
}
