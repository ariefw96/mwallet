package com.sendiri.mwallet_user.repo;

import com.sendiri.mwallet_repo.entity.OtpEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OTPRepository extends JpaRepository<OtpEntity, UUID> {

    Optional<OtpEntity> findFirstByPhoneNoAndIsUsedOrderByCreatedAtDesc(String phoneNo, Boolean isUsed);

}
