package com.shhwang0930.mytg.board.controller;

import com.shhwang0930.mytg.board.model.BoardDTO;
import com.shhwang0930.mytg.board.model.BoardEntity;
import com.shhwang0930.mytg.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;

    @GetMapping("/list")
    public List<BoardDTO> readBoardList(){
        List<BoardEntity> boardList = boardService.readBoardList();
        List<BoardDTO> boardDTOList = boardService.toDTO(boardList);
        return boardDTOList;
    }

    @GetMapping("/{idx}")
    public BoardDTO readBoard(@PathVariable Long idx){
        BoardEntity board = boardService.readBoard(idx);
        BoardDTO boardDTO = boardService.toDTO(board);
        return boardDTO;
    }

    @PostMapping()
    public void createBoard(@RequestBody BoardDTO boardDTO){
        boardService.createBoard(boardDTO);
    }

    @PutMapping("/{idx}")
    public void updateBoard(@PathVariable Long idx, @RequestBody BoardDTO boardDTO){
        boardService.updateBoard(idx, boardDTO);
    }

    @DeleteMapping("/{idx}")
    public void deleteBoard(@PathVariable Long idx){
        boardService.deleteBoard(idx);
    }
}
