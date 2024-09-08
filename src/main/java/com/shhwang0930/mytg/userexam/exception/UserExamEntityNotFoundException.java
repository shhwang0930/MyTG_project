package com.shhwang0930.mytg.userexam.exception;

public class UserExamEntityNotFoundException extends RuntimeException{
    public UserExamEntityNotFoundException() {
        super("UserExam entity not found.");
    }
}
