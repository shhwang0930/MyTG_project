package com.shhwang0930.mytg.tc;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {
    @GetMapping("/")
    public String mainP(){
        return "main c";
    }
}
