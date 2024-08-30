package com.shhwang0930.mytg.userexam.controller;

import com.shhwang0930.mytg.common.model.ResponseMessage;
import com.shhwang0930.mytg.common.model.StatusCode;
import com.shhwang0930.mytg.userexam.model.UserExamDTO;
import com.shhwang0930.mytg.userexam.service.UserExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/us-ex")
@RequiredArgsConstructor
public class UserExamController {
    private final UserExamService userExamService;

    @GetMapping("/me/list")
    public ResponseEntity<ResponseMessage> readUserExamList(){
        StatusCode statusCode = StatusCode.SUCCESS;
        ResponseMessage responseMessage = new ResponseMessage(statusCode.getCode(), statusCode.getMessage(), null);
        responseMessage.addData("userExam", userExamService.readUserExamList());
        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }

    @GetMapping("/me/{userExamId}")
    public ResponseEntity<ResponseMessage> readUserExam(@PathVariable int userExamId){
        if(!userExamService.userExamIsExist(userExamId)){
            StatusCode statusCode = StatusCode.USER_EXAM_NOT_FOUND;
            ResponseMessage responseMessage = new ResponseMessage(statusCode.getCode(), statusCode.getMessage(), null);
            return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
        }

        StatusCode statusCode = StatusCode.SUCCESS;
        ResponseMessage responseMessage = new ResponseMessage(statusCode.getCode(), statusCode.getMessage(), null);
        responseMessage.addData("userExam", userExamService.readUserExam(userExamId));
        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }

    @PostMapping("/me")
    public ResponseEntity<ResponseMessage> createUserExam(@RequestBody UserExamDTO userExamDTO){
        StatusCode statusCode = StatusCode.SUCCESS;
        ResponseMessage responseMessage = new ResponseMessage(statusCode.getCode(), statusCode.getMessage(), null);
        userExamService.createUserExam(userExamDTO);
        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }

    @PutMapping("/me/{userExamId}")
    public ResponseEntity<ResponseMessage> updateUserExam(@RequestBody UserExamDTO userExamDTO, @PathVariable int userExamId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        if(!userExamService.matchUser(username, userExamId)){
            StatusCode statusCode = StatusCode.USER_MATCH_FAILED;
            ResponseMessage responseMessage = new ResponseMessage(statusCode.getCode(), statusCode.getMessage(), null);
            return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
        }

        if(!userExamService.userExamIsExist(userExamId)){
            StatusCode statusCode = StatusCode.USER_EXAM_NOT_FOUND;
            ResponseMessage responseMessage = new ResponseMessage(statusCode.getCode(), statusCode.getMessage(), null);
            return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
        }

        StatusCode statusCode = StatusCode.SUCCESS;
        ResponseMessage responseMessage = new ResponseMessage(statusCode.getCode(), statusCode.getMessage(), null);
        userExamService.updateUserExam(userExamDTO, userExamId);
        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }

    @DeleteMapping("/me/{userExamId}")
    public ResponseEntity<ResponseMessage> deleteUserExam(@PathVariable int userExamId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        if(!userExamService.matchUser(username, userExamId)){
            StatusCode statusCode = StatusCode.USER_MATCH_FAILED;
            ResponseMessage responseMessage = new ResponseMessage(statusCode.getCode(), statusCode.getMessage(), null);
            return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
        }

        if(!userExamService.userExamIsExist(userExamId)){
            StatusCode statusCode = StatusCode.USER_EXAM_NOT_FOUND;
            ResponseMessage responseMessage = new ResponseMessage(statusCode.getCode(), statusCode.getMessage(), null);
            return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
        }

        StatusCode statusCode = StatusCode.SUCCESS;
        ResponseMessage responseMessage = new ResponseMessage(statusCode.getCode(), statusCode.getMessage(), null);
        userExamService.deleteUserExam(userExamId);
        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }
}
