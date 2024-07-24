package com.shhwang0930.mytg.comment.model;

import com.shhwang0930.mytg.board.model.BoardEntity;
import com.shhwang0930.mytg.user.model.UserEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentIdx;

    @Column
    private String commentDesc;

    @ManyToOne
    @JoinColumn(name = "idx")
    private BoardEntity board;

    @ManyToOne
    @JoinColumn(name = "id")
    private UserEntity user;

    @Builder
    public CommentEntity(Long commentIdx, String commentDesc, BoardEntity board, UserEntity user) {
        this.commentIdx = commentIdx;
        this.commentDesc = commentDesc;
        this.board = board;
        this.user = user;
    }
}
