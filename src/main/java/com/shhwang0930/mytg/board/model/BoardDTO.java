package com.shhwang0930.mytg.board.model;

import lombok.Data;

@Data
public class BoardDTO {
    private String title;
    private String content;
    private String category;
    private BoardResponseDTO user;
}
