package com.shhwang0930.mytg.board.model;

import com.shhwang0930.mytg.user.model.UserEntity;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class BoardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column
    private String title;

    @Column
    private String content;

    @Column
    private String category;

    @ManyToOne
    @JoinColumn(name = "id")
    private UserEntity user;
}
