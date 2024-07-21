package com.shhwang0930.mytg.board.service;

import com.shhwang0930.mytg.board.model.BoardDTO;
import com.shhwang0930.mytg.board.model.BoardEntity;
import com.shhwang0930.mytg.board.model.BoardResponseDTO;
import com.shhwang0930.mytg.board.repository.BoardRepository;
import com.shhwang0930.mytg.user.model.CustomUserDetails;
import com.shhwang0930.mytg.user.model.UserEntity;
import com.shhwang0930.mytg.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    public List<BoardEntity> readBoardList(){
        return boardRepository.findAll();
    }

    public BoardEntity readBoard(long idx){
        return boardRepository.findAllByIdx(idx);
    }

    public void createBoard(BoardDTO boardDTO){

        // 현재 인증된 사용자 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        UserEntity user = userRepository.findByUsername(username);

        String title = boardDTO.getTitle();
        String content = boardDTO.getContent();
        String category = boardDTO.getCategory();

        BoardEntity board = new BoardEntity();

        board.setTitle(title);
        board.setContent(content);
        board.setCategory(category);
        board.setUser(user);
        boardRepository.save(board);
    }

    public void updateBoard(Long idx, BoardDTO boardDTO){
        String title = boardDTO.getTitle();
        String content = boardDTO.getContent();
        String category = boardDTO.getCategory();

        BoardEntity board = boardRepository.findAllByIdx(idx);
        // board isExists
        board.setTitle(title);
        board.setContent(content);
        board.setCategory(category);

        boardRepository.save(board);
    }

    public void deleteBoard(long idx){
        boardRepository.deleteByIdx(idx);
    }

    public BoardDTO toDTO(BoardEntity boardEntity) {
        BoardResponseDTO userDTO = new BoardResponseDTO(boardEntity.getUser().getUsername());
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setTitle(boardEntity.getTitle());
        boardDTO.setContent(boardEntity.getContent());
        boardDTO.setCategory(boardEntity.getCategory());
        boardDTO.setUser(userDTO);
        return boardDTO;
    }

    public List<BoardDTO> toDTO(List<BoardEntity> boardEntities) {
        return boardEntities.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
