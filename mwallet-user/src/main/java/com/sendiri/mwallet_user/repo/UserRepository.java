package com.sendiri.mwallet_user.repo;

import com.sendiri.mwallet_repo.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
}
