package com.shhwang0930.mytg.exam.controller;

import com.shhwang0930.mytg.exam.model.dto.DateDTO;
import com.shhwang0930.mytg.exam.service.ExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ExamController {
    private final ExamService examService;

    @GetMapping("/test")
    public void examList() throws Exception {
        examService.examList();
    }

    @GetMapping("/schedule/{seriesnm}")
    public List<DateDTO> examSchedule(@PathVariable String seriesnm) throws Exception {
        return examService.examSchedule(seriesnm);
    }
}
