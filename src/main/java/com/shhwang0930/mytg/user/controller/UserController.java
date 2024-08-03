package com.shhwang0930.mytg.user.controller;

import com.shhwang0930.mytg.common.model.ResponseMessage;
import com.shhwang0930.mytg.common.model.StatusCode;
import com.shhwang0930.mytg.user.model.dto.JoinDTO;
import com.shhwang0930.mytg.user.model.dto.UpdatePasswordDTO;
import com.shhwang0930.mytg.user.model.dto.UsernameDTO;
import com.shhwang0930.mytg.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
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

    @PostMapping("/password")
    public ResponseEntity<ResponseMessage> pwfind(@RequestBody UsernameDTO usernameDTO){
        if(usernameDTO == null||!userService.existUsername(usernameDTO.getUsername())){
            StatusCode statusCode = StatusCode.FIND_USER_FAILED;
            ResponseMessage responseMessage = new ResponseMessage(StatusCode.SUCCESS.getCode(), statusCode.getMessage(), null);
            return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
        }
        StatusCode statusCode = StatusCode.SUCCESS;
        ResponseMessage responseMessage = new ResponseMessage(statusCode.getCode(), statusCode.getMessage(), null);
        responseMessage.addData("password",userService.generatePassword(usernameDTO.getUsername()));
        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }

    @PutMapping("/me/password")
    public ResponseEntity<ResponseMessage> pwUpdate(@RequestBody UpdatePasswordDTO updatePasswordDTO){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        if(!userService.existUsername(username)){
            StatusCode statusCode = StatusCode.FIND_USER_FAILED;
            ResponseMessage responseMessage = new ResponseMessage(statusCode.getCode(), statusCode.getMessage(), null);
            return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
        }

        String oldPw = updatePasswordDTO.getOldPassword();
        String newPw = updatePasswordDTO.getNewPassword();
        if(oldPw.equals(newPw)){
            StatusCode statusCode = StatusCode.USER_MATCH_FAILED;
            ResponseMessage responseMessage = new ResponseMessage(statusCode.getCode(), statusCode.getMessage(),null);
            return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
        }

        userService.updatePassword(username, newPw);
        StatusCode statusCode = StatusCode.SUCCESS;
        ResponseMessage responseMessage = new ResponseMessage(statusCode.getCode(), statusCode.getMessage(), null);
        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }
}
