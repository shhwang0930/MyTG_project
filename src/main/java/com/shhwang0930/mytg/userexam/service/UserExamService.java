package com.shhwang0930.mytg.userexam.service;

import com.shhwang0930.mytg.board.exception.BoardEntityNotFoundException;
import com.shhwang0930.mytg.board.model.entity.BoardEntity;
import com.shhwang0930.mytg.exam.model.entity.ExamEntity;
import com.shhwang0930.mytg.exam.repository.ExamRepository;
import com.shhwang0930.mytg.user.model.entity.UserEntity;
import com.shhwang0930.mytg.user.repository.UserRepository;
import com.shhwang0930.mytg.userexam.exception.UserExamEntityNotFoundException;
import com.shhwang0930.mytg.userexam.model.UserExamDTO;
import com.shhwang0930.mytg.userexam.model.UserExamEntity;
import com.shhwang0930.mytg.userexam.repository.UserExamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserExamService {
    private final UserExamRepository userExamRepository;
    private final UserRepository userRepository;
    private final ExamRepository examRepository;

    public List<UserExamDTO> readUserExamList(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        int userId = userRepository.findByUsername(username).getId();

        List<UserExamEntity> userExamList = userExamRepository.findByUserId(userId);
        return UserExamDTO.fromEntityList(userExamList);
    }

    public UserExamDTO readUserExam(int userExamId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        int userId = userRepository.findByUsername(username).getId();

        UserExamEntity userExam = userExamRepository.findByUserExamId(userExamId);
        return UserExamDTO.fromEntity(userExam);
    }

    @Transactional
    public UserExamDTO createUserExam(UserExamDTO userExamDTO){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        UserEntity user = userRepository.findByUsername(username);
        ExamEntity exam = examRepository.findByJmcd(userExamDTO.getJmcd());
        UserExamEntity userExam = userExamDTO.toEntity(user, exam);

        return UserExamDTO.fromEntity(userExamRepository.save(userExam));
    }

    @Transactional
    public UserExamDTO updateUserExam(UserExamDTO userExamDTO, int userExamId){
        // 현재 인증된 사용자 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // 현재 인증된 사용자 가져오기
        UserEntity user = userRepository.findByUsername(username);

        // 기존 게시물 조회
        UserExamEntity userExam = userExamRepository.findById(userExamId)
                .orElseThrow(UserExamEntityNotFoundException::new);

        // 게시물의 작성자와 현재 인증된 사용자가 일치하는지 확인
        if (!userExam.getUser().equals(user)) {
            throw new RuntimeException("Unauthorized to update this userExam");
        }

        // 새로운 상태를 가진 UserExamEntity 생성
        UserExamEntity updatedUserExam = UserExamEntity.builder()
                .userExamId(userExamId)
                .user(user)
                .exam(examRepository.findByJmcd(userExamDTO.getJmcd()))
                .date(userExamDTO.getExamDate())
                .build();

        // 새로운 엔티티로 저장
        return UserExamDTO.fromEntity(userExamRepository.save(updatedUserExam));
    }

    @Transactional
    public void deleteUserExam(int userExamId){
        // 현재 인증된 사용자 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // 현재 인증된 사용자 가져오기
        UserEntity user = userRepository.findByUsername(username);

        // 기존 게시물 조회
        UserExamEntity userExam = userExamRepository.findById(userExamId)
                .orElseThrow(UserExamEntityNotFoundException::new);

        // 게시물의 작성자와 현재 인증된 사용자가 일치하는지 확인
        if (!userExam.getUser().equals(user)) {
            throw new RuntimeException("Unauthorized to update this userExam");
        }
        userExamRepository.deleteByUserExamId(userExamId);
    }

    public boolean userExamIsExist(int userExamId){
        return userExamRepository.existsByUserExamId(userExamId);
    }

    public boolean matchUser(String username, int userExamId){
        if(userRepository.findByUsername(username).getId()==userExamRepository.findByUserExamId(userExamId).getUser().getId()){
            return true;
        }
        return false;
    }
}
