package com.shhwang0930.mytg.board.controller;

import com.shhwang0930.mytg.board.model.BoardDTO;
import com.shhwang0930.mytg.board.model.BoardEntity;
import com.shhwang0930.mytg.board.service.BoardService;
import com.shhwang0930.mytg.common.model.ResponseMessage;
import com.shhwang0930.mytg.common.model.StatusCode;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;

    @GetMapping("/list")
    public ResponseEntity<ResponseMessage> readBoardList(){
        StatusCode statusCode = StatusCode.SUCCESS;
        ResponseMessage responseMessage = new ResponseMessage(statusCode.getCode(), statusCode.getMessage(), null);
        responseMessage.addData("board", boardService.readBoardList());
        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }

    @GetMapping("/{idx}")
    public ResponseEntity<ResponseMessage> readBoard(@PathVariable Long idx){
        if(!boardService.boardIsExist(idx)){
            StatusCode statusCode = StatusCode.BOARD_NOT_FOUND;
            ResponseMessage responseMessage = new ResponseMessage(statusCode.getCode(), statusCode.getMessage(), null);
            return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
        }
        StatusCode statusCode = StatusCode.SUCCESS;
        ResponseMessage responseMessage = new ResponseMessage(statusCode.getCode(), statusCode.getMessage(), null);
        responseMessage.addData("board", boardService.readBoard(idx));
        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }

    @PostMapping()
    public ResponseEntity<ResponseMessage> createBoard(@RequestBody BoardDTO boardDTO){
        StatusCode statusCode = StatusCode.SUCCESS;
        ResponseMessage responseMessage = new ResponseMessage(statusCode.getCode(), statusCode.getMessage(), null);
        boardService.createBoard(boardDTO);
        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }

    @PutMapping("/{idx}")
    public ResponseEntity<ResponseMessage> updateBoard(@PathVariable Long idx, @RequestBody BoardDTO boardDTO){
        if(!boardService.boardIsExist(idx)){
            StatusCode statusCode = StatusCode.BOARD_NOT_FOUND;
            ResponseMessage responseMessage = new ResponseMessage(statusCode.getCode(), statusCode.getMessage(), null);
            return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
        }
        StatusCode statusCode = StatusCode.SUCCESS;
        ResponseMessage responseMessage = new ResponseMessage(statusCode.getCode(), statusCode.getMessage(), null);
        boardService.updateBoard(idx, boardDTO);
        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }

    @DeleteMapping("/{idx}")
    public ResponseEntity<ResponseMessage> deleteBoard(@PathVariable Long idx){
        if(!boardService.boardIsExist(idx)){
            StatusCode statusCode = StatusCode.BOARD_NOT_FOUND;
            ResponseMessage responseMessage = new ResponseMessage(statusCode.getCode(), statusCode.getMessage(), null);
            return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
        }
        StatusCode statusCode = StatusCode.SUCCESS;
        ResponseMessage responseMessage = new ResponseMessage(statusCode.getCode(), statusCode.getMessage(), null);
        boardService.deleteBoard(idx);
        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }

    @GetMapping("/search")
    public List<BoardDTO> searchBoard(@RequestParam String keyword){
        return boardService.searchBoard(keyword);
    }
}
