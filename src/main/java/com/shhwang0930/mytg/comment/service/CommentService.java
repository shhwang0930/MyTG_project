package com.shhwang0930.mytg.comment.service;


import com.shhwang0930.mytg.board.exception.BoardEntityNotFoundException;
import com.shhwang0930.mytg.board.model.dto.BoardDTO;
import com.shhwang0930.mytg.board.model.entity.BoardEntity;
import com.shhwang0930.mytg.board.repository.BoardRepository;
import com.shhwang0930.mytg.comment.exception.CommentEntityNotFoundException;
import com.shhwang0930.mytg.comment.model.CommentDTO;
import com.shhwang0930.mytg.comment.model.CommentEntity;
import com.shhwang0930.mytg.comment.repository.CommentRepository;
import com.shhwang0930.mytg.user.model.entity.UserEntity;
import com.shhwang0930.mytg.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    public List<CommentDTO> readCommentList(Long idx){
        List<CommentEntity> commentList = commentRepository.findAllByBoardIdx(idx);
        //board idx isExist
        return CommentDTO.fromEntityList(commentList);
    }

    public CommentDTO readComment(Long commentIdx){
        CommentEntity commentEntity = commentRepository.findById(commentIdx)
                .orElseThrow(CommentEntityNotFoundException::new);
        return CommentDTO.fromEntity(commentEntity);
    }

    public CommentDTO createComment(CommentDTO commentDTO, Long idx){
        // 현재 인증된 사용자 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        UserEntity user = userRepository.findByUsername(username);
        BoardEntity board = boardRepository.findAllByIdx(idx);

        CommentEntity comment = commentDTO.toEntity(board, user);

        return CommentDTO.fromEntity(commentRepository.save(comment));
    }

    public CommentDTO updateComment(CommentDTO commentDTO, Long commentIdx, Long idx){
        // 현재 인증된 사용자 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // 현재 인증된 사용자 가져오기
        UserEntity user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        BoardEntity board = boardRepository.findAllByIdx(idx);

        // 기존 게시물 조회
        CommentEntity commentEntity = commentRepository.findById(commentIdx)
                .orElseThrow(CommentEntityNotFoundException::new);


        // 게시물의 작성자와 현재 인증된 사용자가 일치하는지 확인
        if (!commentEntity.getUser().equals(user)) {
            throw new RuntimeException("Unauthorized to update this comment");
        }

        // 새로운 상태를 가진 BoardEntity를 생성
        CommentEntity updatedComment = CommentEntity.builder()
                .commentIdx(commentEntity.getCommentIdx()) // 기존 ID 유지
                .commentDesc(commentDTO.getDesc())
                .user(user) // 현재 사용자로 설정
                .board(board)
                .build();


        // 새로운 엔티티로 저장
        return CommentDTO.fromEntity(commentRepository.save(updatedComment));
    }

    public void deleteComment(Long commentIdx){
        // 현재 인증된 사용자 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // 기존 게시물 조회
        CommentEntity commentEntity = commentRepository.findById(commentIdx)
                .orElseThrow(CommentEntityNotFoundException::new);

        // 현재 인증된 사용자 가져오기
        UserEntity user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        // 게시물의 작성자와 현재 인증된 사용자가 일치하는지 확인
        if (!commentEntity.getUser().equals(user)) {
            throw new RuntimeException("Unauthorized to update this comment");
        }
        commentRepository.deleteByCommentIdx(commentIdx);
    }

    public boolean existComment(Long commentIdx){
        return commentRepository.existsByCommentIdx(commentIdx);
    }

    public boolean matchBoardComment(Long idx, Long commentIdx){
        CommentEntity comment = commentRepository.findByCommentIdx(commentIdx);
        Long boardIdx = comment.getBoard().getIdx();
        if(idx == boardIdx){
            return true;
        }
        return false;
    }

    public boolean matchUser(String username, Long commentIdx){
        if(userRepository.findByUsername(username).getId()==commentRepository.findByCommentIdx(commentIdx).getUser().getId()){
            return true;
        }
        return false;
    }
}
