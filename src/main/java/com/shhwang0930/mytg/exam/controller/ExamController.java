package com.shhwang0930.mytg.exam.controller;

import com.shhwang0930.mytg.common.model.ResponseMessage;
import com.shhwang0930.mytg.common.model.StatusCode;
import com.shhwang0930.mytg.exam.model.dto.DateDTO;
import com.shhwang0930.mytg.exam.service.ExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/exam")
public class ExamController {
    private final ExamService examService;

    @GetMapping("/list")
    public ResponseEntity<ResponseMessage> examList() throws Exception {
        StatusCode statusCode = StatusCode.SUCCESS;
        ResponseMessage responseMessage = new ResponseMessage(statusCode.getCode(), statusCode.getMessage(), null);
        examService.examList();
        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }

    @GetMapping("/schedule/{seriesnm}")
    public ResponseEntity<ResponseMessage> examSchedule(@PathVariable String seriesnm) throws Exception {
        StatusCode statusCode = StatusCode.SUCCESS;
        ResponseMessage responseMessage = new ResponseMessage(statusCode.getCode(), statusCode.getMessage(), null);
        responseMessage.addData("examDate", examService.examSchedule(seriesnm));
        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }
}
