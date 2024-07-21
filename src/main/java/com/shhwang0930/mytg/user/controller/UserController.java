package com.shhwang0930.mytg.user.controller;

import com.shhwang0930.mytg.user.model.JoinDTO;
import com.shhwang0930.mytg.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/join")
    public String userJoin(@RequestBody JoinDTO joinDTO){
        userService.joinUser(joinDTO);
        return "success";
    }
}
