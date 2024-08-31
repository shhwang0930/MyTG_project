package com.shhwang0930.mytg.board.exception;

public class BoardEntityNotFoundException extends RuntimeException{
    public BoardEntityNotFoundException() {
        super("Board entity not found.");
    }
}
