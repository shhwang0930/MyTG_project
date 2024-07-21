package com.shhwang0930.mytg.jwt.service;

import com.shhwang0930.mytg.jwt.model.RefreshToken;
import com.shhwang0930.mytg.jwt.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void saveRefreshToken(String refreshToken, String username){
        refreshTokenRepository.save(new RefreshToken(refreshToken, username));
    }

    @Transactional
    public Boolean isExistRefreshToken(String refreshToken){
        return refreshTokenRepository.existsById(refreshToken);
    }

    @Transactional
    public void deleteRefreshToken(String refreshToken){
        refreshTokenRepository.deleteById(refreshToken);
    }
}
