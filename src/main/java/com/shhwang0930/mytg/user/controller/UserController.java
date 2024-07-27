package com.shhwang0930.mytg.user.controller;

import com.shhwang0930.mytg.common.model.ResponseMessage;
import com.shhwang0930.mytg.common.model.StatusCode;
import com.shhwang0930.mytg.user.model.JoinDTO;
import com.shhwang0930.mytg.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity<ResponseMessage> userJoin(@RequestBody JoinDTO joinDTO){
        if(userService.existUsername(joinDTO.getUsername())){
            StatusCode statusCode = StatusCode.RESOURCE_ALREADY_EXISTS;
            ResponseMessage responseMessage = new ResponseMessage(statusCode.getCode(), statusCode.getMessage(), null);
            return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
        }
        StatusCode statusCode = StatusCode.SUCCESS;
        ResponseMessage responseMessage = new ResponseMessage(statusCode.getCode(), statusCode.getMessage(), null);
        userService.joinUser(joinDTO);
        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }
}
