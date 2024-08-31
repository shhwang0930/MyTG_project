package com.shhwang0930.mytg.board.service;

import com.shhwang0930.mytg.board.exception.BoardEntityNotFoundException;
import com.shhwang0930.mytg.board.model.dto.BoardDTO;
import com.shhwang0930.mytg.board.model.entity.BoardEntity;
import com.shhwang0930.mytg.board.model.entity.Category;
import com.shhwang0930.mytg.board.repository.BoardRepository;
import com.shhwang0930.mytg.user.model.entity.UserEntity;
import com.shhwang0930.mytg.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    public List<BoardDTO> readBoardList(){
        List<BoardEntity> boardList = boardRepository.findAll();
        return BoardDTO.fromEntityList(boardList);
    }

    public BoardDTO readBoard(long idx){
        BoardEntity boardEntity = boardRepository.findById(idx)
                .orElseThrow(BoardEntityNotFoundException::new);
        return BoardDTO.fromEntity(boardEntity);
    }

    @Transactional
    public void createBoard(BoardDTO boardDTO){

        // 현재 인증된 사용자 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        UserEntity user = userRepository.findByUsername(username);

        BoardEntity board = boardDTO.toEntity(user);
        boardRepository.save(board);
    }

    @Transactional
    public void updateBoard(Long idx, BoardDTO boardDTO){
        // 현재 인증된 사용자 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // 현재 인증된 사용자 가져오기
        UserEntity userEntity = userRepository.findByUsername(username);
        if (userEntity == null) {
            throw new RuntimeException("User not found");
        }

        // 기존 게시물 조회
        BoardEntity boardEntity = boardRepository.findById(idx)
                .orElseThrow(BoardEntityNotFoundException::new);

        // 게시물의 작성자와 현재 인증된 사용자가 일치하는지 확인
        if (!boardEntity.getUser().equals(userEntity)) {
            throw new RuntimeException("Unauthorized to update this board");
        }

        // 새로운 상태를 가진 BoardEntity를 생성
        BoardEntity updatedBoard = BoardEntity.builder()
                .idx(boardEntity.getIdx()) // 기존 ID 유지
                .title(boardDTO.getTitle())
                .content(boardDTO.getContent())
                .category(Category.valueOf(boardDTO.getCategory()))
                .user(userEntity) // 현재 사용자로 설정
                .build();

        // 새로운 엔티티로 저장
        boardRepository.save(updatedBoard);
    }

    @Transactional
    public void deleteBoard(long idx){
        boardRepository.deleteByIdx(idx);
    }

    public boolean boardIsExist(long idx){
        return boardRepository.existsByIdx(idx);
    }

    public List<BoardDTO> searchBoard(String keyword) {
        return boardRepository.searchByTitleOrContent(keyword);
    }

    public boolean matchUser(String username, Long idx){
        if(userRepository.findByUsername(username).getId()==boardRepository.findAllByIdx(idx).getUser().getId()){
            return true;
        }
        return false;
    }
}
