package com.shhwang0930.mytg.board.repository;

import com.shhwang0930.mytg.board.model.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardRepository extends JpaRepository<BoardEntity, Long>, BoardRepositoryCustom{
    List<BoardEntity> findAll();
    BoardEntity findAllByIdx(Long idx);
    void deleteByIdx(Long idx);

    boolean existsByIdx(Long idx);
}
