package com.shhwang0930.mytg.jwt.repository;

import com.shhwang0930.mytg.jwt.model.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
}
