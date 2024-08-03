package com.shhwang0930.mytg.jwt.service;

import com.shhwang0930.mytg.user.model.entity.CustomUserDetails;
import com.shhwang0930.mytg.user.model.entity.UserEntity;
import com.shhwang0930.mytg.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username);
        }
        return new CustomUserDetails(user);
    }
}
