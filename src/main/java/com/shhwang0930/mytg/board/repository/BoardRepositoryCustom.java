package com.shhwang0930.mytg.board.repository;

import com.shhwang0930.mytg.board.model.dto.BoardDTO;

import java.util.List;

public interface BoardRepositoryCustom{
    List<BoardDTO> searchByTitleOrContent(String keyword);
}
