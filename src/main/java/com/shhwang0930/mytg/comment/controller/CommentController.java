package com.shhwang0930.mytg.comment.controller;

import com.shhwang0930.mytg.board.service.BoardService;
import com.shhwang0930.mytg.comment.model.CommentDTO;
import com.shhwang0930.mytg.comment.service.CommentService;
import com.shhwang0930.mytg.common.model.ResponseMessage;
import com.shhwang0930.mytg.common.model.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final BoardService boardService;

    @GetMapping("/list/{idx}")
    public ResponseEntity<ResponseMessage> readCommentList(@PathVariable Long idx){
        if(!boardService.boardIsExist(idx)){
            StatusCode statusCode = StatusCode.BOARD_NOT_FOUND;
            ResponseMessage responseMessage = new ResponseMessage(statusCode.getCode(), statusCode.getMessage(), null);
            return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
        }

        StatusCode statusCode = StatusCode.SUCCESS;
        ResponseMessage responseMessage = new ResponseMessage(statusCode.getCode(), statusCode.getMessage(), null);
        responseMessage.addData("board", commentService.readCommentList(idx));
        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }

    @GetMapping("/{idx}/{commentIdx}")
    public ResponseEntity<ResponseMessage> readComment(@PathVariable Long idx, @PathVariable Long commentIdx){
        if(!commentService.matchBoardComment(idx, commentIdx)){
            StatusCode statusCode = StatusCode.BOARD_COMMENT_NOT_MATCH;
            ResponseMessage responseMessage = new ResponseMessage(statusCode.getCode(), statusCode.getMessage(), null);
            return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
        }

        if(!commentService.existComment(commentIdx)){
            StatusCode statusCode = StatusCode.COMMENT_NOT_FOUND;
            ResponseMessage responseMessage = new ResponseMessage(statusCode.getCode(), statusCode.getMessage(), null);
            return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
        }

        StatusCode statusCode = StatusCode.SUCCESS;
        ResponseMessage responseMessage = new ResponseMessage(statusCode.getCode(), statusCode.getMessage(), null);
        responseMessage.addData("board", commentService.readComment(commentIdx));
        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }

    @PostMapping("/me/{idx}")
    public ResponseEntity<ResponseMessage> createComment(@PathVariable Long idx, @RequestBody CommentDTO commentDTO){
        if(!boardService.boardIsExist(idx)){
            StatusCode statusCode = StatusCode.BOARD_NOT_FOUND;
            ResponseMessage responseMessage = new ResponseMessage(statusCode.getCode(), statusCode.getMessage(), null);
            return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
        }

        StatusCode statusCode = StatusCode.SUCCESS;
        ResponseMessage responseMessage = new ResponseMessage(statusCode.getCode(), statusCode.getMessage(), null);
        commentService.createComment(commentDTO, idx);
        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }

    @PutMapping("/me/{idx}/{commentIdx}")
    public ResponseEntity<ResponseMessage> updateComment( @PathVariable Long idx, @PathVariable Long commentIdx, @RequestBody CommentDTO commentDTO){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        if(!commentService.matchUser(username, commentIdx)){
            StatusCode statusCode = StatusCode.USER_MATCH_FAILED;
            ResponseMessage responseMessage = new ResponseMessage(statusCode.getCode(), statusCode.getMessage(), null);
            return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
        }

        if(!commentService.matchBoardComment(idx, commentIdx)){
            StatusCode statusCode = StatusCode.BOARD_COMMENT_NOT_MATCH;
            ResponseMessage responseMessage = new ResponseMessage(statusCode.getCode(), statusCode.getMessage(), null);
            return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
        }

        if(!commentService.existComment(commentIdx)){
            StatusCode statusCode = StatusCode.COMMENT_NOT_FOUND;
            ResponseMessage responseMessage = new ResponseMessage(statusCode.getCode(), statusCode.getMessage(), null);
            return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
        }

        StatusCode statusCode = StatusCode.SUCCESS;
        ResponseMessage responseMessage = new ResponseMessage(statusCode.getCode(), statusCode.getMessage(), null);
        commentService.updateComment(commentDTO, commentIdx, idx);
        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }

    @DeleteMapping("/me/{idx}/{commentIdx}")
    public ResponseEntity<ResponseMessage> deleteComment(@PathVariable Long commentIdx, @PathVariable Long idx) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        if(!commentService.matchUser(username, commentIdx)){
            StatusCode statusCode = StatusCode.USER_MATCH_FAILED;
            ResponseMessage responseMessage = new ResponseMessage(statusCode.getCode(), statusCode.getMessage(), null);
            return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
        }

        if (!commentService.matchBoardComment(idx, commentIdx)) {
            StatusCode statusCode = StatusCode.BOARD_COMMENT_NOT_MATCH;
            ResponseMessage responseMessage = new ResponseMessage(statusCode.getCode(), statusCode.getMessage(), null);
            return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
        }

        if (!commentService.existComment(commentIdx)) {
            StatusCode statusCode = StatusCode.COMMENT_NOT_FOUND;
            ResponseMessage responseMessage = new ResponseMessage(statusCode.getCode(), statusCode.getMessage(), null);
            return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
        }

        StatusCode statusCode = StatusCode.SUCCESS;
        ResponseMessage responseMessage = new ResponseMessage(statusCode.getCode(), statusCode.getMessage(), null);
        commentService.deleteComment(commentIdx);
        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }
}
