package com.shhwang0930.mytg.comment.controller;

import com.shhwang0930.mytg.comment.model.CommentDTO;
import com.shhwang0930.mytg.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/list/{idx}")
    public List<CommentDTO> readCommentList(@PathVariable Long idx){
        return commentService.readCommentList(idx);
    }

    @GetMapping("/{idx}/{commentIdx}")
    public CommentDTO readComment(@PathVariable Long idx, @PathVariable Long commentIdx){
        //board idx도 받아 예외처리 필요
        return commentService.readComment(commentIdx);
    }

    @PostMapping("/{idx}")
    public void createComment(@PathVariable Long idx, @RequestBody CommentDTO commentDTO){
        commentService.createComment(commentDTO, idx);
    }

    @PutMapping("/{idx}/{commentIdx}")
    public void updateComment( @PathVariable Long idx, @PathVariable Long commentIdx,@RequestBody CommentDTO commentDTO){
        // 추가 로그
        System.out.println("commentDTO: " + commentDTO);
        System.out.println("commentDTO.getDesc(): " + commentDTO.getDesc());
        commentService.updateComment(commentDTO, commentIdx, idx);
    }

    @DeleteMapping("/{idx}/{commentIdx}")
    public void deleteComment(@PathVariable Long commentIdx){
        commentService.deleteComment(commentIdx);
    }
}
