package com.shhwang0930.mytg.board.model;

import lombok.Data;

@Data
public class BoardResponseDTO {
    private String username;
    public BoardResponseDTO(String username) {
        this.username = username;
    }

}
