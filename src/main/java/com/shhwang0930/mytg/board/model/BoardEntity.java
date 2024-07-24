package com.shhwang0930.mytg.board.model;

import com.shhwang0930.mytg.comment.model.CommentEntity;
import com.shhwang0930.mytg.user.model.UserEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
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

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
    private List<CommentEntity> commentList;

    @Builder
    public BoardEntity(Long idx, String title, String content, String category, UserEntity user) {
        this.idx = idx;
        this.title = title;
        this.content = content;
        this.category = category;
        this.user = user;
    }
}
