package com.shhwang0930.mytg.user.service;

import com.shhwang0930.mytg.user.model.dto.JoinDTO;
import com.shhwang0930.mytg.user.model.dto.PasswordDTO;
import com.shhwang0930.mytg.user.model.entity.UserEntity;
import com.shhwang0930.mytg.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private static final char[] rndAllCharacters = new char[]{
            //number
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            //uppercase
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            //lowercase
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            //special symbols
            '@', '$', '!', '%', '*', '?', '&'
    };

    @Transactional
    public String joinUser(JoinDTO joinDTO){
        String username = joinDTO.getUsername();
        String password = joinDTO.getPassword();

        UserEntity data = new UserEntity();

        data.setUsername(username);
        data.setPassword(bCryptPasswordEncoder.encode(password));
        data.setRole("ROLE_ADMIN");

        userRepository.save(data);

        return "success";
    }

    public boolean existUsername(String username){
        return userRepository.existsByUsername(username);
    }

    @Transactional
    public PasswordDTO generatePassword(String username){
        //랜덤 비밀번호 생성
        SecureRandom random = new SecureRandom();
        StringBuilder stringBuilder = new StringBuilder();

        int rndAllCharactersLength = rndAllCharacters.length;
        for (int i = 0; i < 10; i++) {
            stringBuilder.append(rndAllCharacters[random.nextInt(rndAllCharactersLength)]);
        }

        //비밀번호 암호화 후 저장
        UserEntity user = userRepository.findByUsername(username);
        user.setPassword(bCryptPasswordEncoder.encode(stringBuilder.toString()));
        userRepository.save(user);

        PasswordDTO passwordDTO = new PasswordDTO();
        passwordDTO.setPassword(stringBuilder.toString());
        return passwordDTO;
    }

    public void updatePassword(String username, String updatePw){
        UserEntity user = userRepository.findByUsername(username);
        user.setPassword(bCryptPasswordEncoder.encode(updatePw));
        userRepository.save(user);
    }
}
