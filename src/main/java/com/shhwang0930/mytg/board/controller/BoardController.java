package com.shhwang0930.mytg.board.controller;

import com.shhwang0930.mytg.board.model.BoardDTO;
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
        return boardService.readBoardList();
    }

    @GetMapping("/{idx}")
    public BoardDTO readBoard(@PathVariable Long idx){
        return boardService.readBoard(idx);
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
