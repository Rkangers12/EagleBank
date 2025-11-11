package com.studio.eaglebank.domain.repositories;

import com.studio.eaglebank.domain.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
