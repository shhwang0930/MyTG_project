package com.shhwang0930.mytg.board.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shhwang0930.mytg.board.model.BoardDTO;
import com.shhwang0930.mytg.board.model.QBoardEntity;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public List<BoardDTO> searchByTitleOrContent(String keyword) {
        QBoardEntity qBoard = QBoardEntity.boardEntity;

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (keyword != null) {
            booleanBuilder.or(qBoard.title.containsIgnoreCase(keyword));
            booleanBuilder.or(qBoard.content.containsIgnoreCase(keyword));
        }
        List<BoardDTO> boards = queryFactory
                .select(Projections.constructor(BoardDTO.class,
                        qBoard.title,
                        qBoard.content,
                        qBoard.category.stringValue(),
                        qBoard.user.username ))
                .from(qBoard)
                .where(booleanBuilder)
                .fetch();

        return boards;
    }


}
