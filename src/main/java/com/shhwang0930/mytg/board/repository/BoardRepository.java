package com.shhwang0930.mytg.board.repository;

import com.shhwang0930.mytg.board.model.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<BoardEntity, Long>, BoardRepositoryCustom{
    List<BoardEntity> findAll();
    BoardEntity findAllByIdx(Long idx);
    void deleteByIdx(Long idx);

    boolean existsByIdx(Long idx);
}
