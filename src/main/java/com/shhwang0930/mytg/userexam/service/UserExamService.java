package com.shhwang0930.mytg.userexam.service;

import com.shhwang0930.mytg.exam.model.entity.ExamEntity;
import com.shhwang0930.mytg.exam.repository.ExamRepository;
import com.shhwang0930.mytg.user.model.entity.UserEntity;
import com.shhwang0930.mytg.user.repository.UserRepository;
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
    public void createUserExam(UserExamDTO userExamDTO){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        UserEntity user = userRepository.findByUsername(username);
        ExamEntity exam = examRepository.findByJmcd(userExamDTO.getJmcd());
        UserExamEntity userExam = userExamDTO.toEntity(user, exam);

        userExamRepository.save(userExam);
    }

    @Transactional
    public void updateUserExam(UserExamDTO userExamDTO, int userExamId){
        // 현재 인증된 사용자 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // 현재 인증된 사용자 가져오기
        UserEntity user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        // 기존 유저시험 조회
        UserExamEntity userExam = userExamRepository.findByUserExamId(userExamId);

        // 새로운 상태를 가진 UserExamEntity 생성
        UserExamEntity updatedUserExam = UserExamEntity.builder()
                .userExamId(userExamId)
                .user(user)
                .exam(examRepository.findByJmcd(userExamDTO.getJmcd()))
                .date(userExamDTO.getExamDate())
                .build();

        // 새로운 엔티티로 저장
        userExamRepository.save(updatedUserExam);
    }

    @Transactional
    public void deleteUserExam(int userExamId){
        // 현재 인증된 사용자 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // 현재 인증된 사용자 가져오기
        UserEntity user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
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
