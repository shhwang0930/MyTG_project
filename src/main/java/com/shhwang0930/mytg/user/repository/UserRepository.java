package com.shhwang0930.mytg.user.repository;

import com.shhwang0930.mytg.user.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    Boolean existsByUsername(String username);

    UserEntity findByUsername(String username);
}
