package com.shhwang0930.mytg.user.service;

import com.shhwang0930.mytg.user.model.JoinDTO;
import com.shhwang0930.mytg.user.model.UserEntity;
import com.shhwang0930.mytg.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

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
}
