package com.shhwang0930.mytg.exam.controller;

import com.shhwang0930.mytg.exam.service.ExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ExamController {
    private final ExamService examService;

    @GetMapping("/test")
    public void examList() throws Exception
    {
        examService.examList();
    }
}
