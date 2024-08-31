package com.shhwang0930.mytg.comment.exception;

public class CommentEntityNotFoundException extends RuntimeException{

    public CommentEntityNotFoundException(){
        super("Comment entity not found.");
    }

}
