package com.sendiri.mwallet_transaction.repo;

import com.sendiri.mwallet_repo.entity.UserEntity;
import com.sendiri.mwallet_repo.entity.WalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public interface WalletRepository extends JpaRepository<WalletEntity, UUID> {

    Optional<WalletEntity> findByUser(UserEntity user);

    @Modifying
    @Query("""
    UPDATE WalletEntity w
    SET w.balance = w.balance - :amount
    WHERE w.user.userId = :userId
    AND w.balance >= :amount
    """)
    int debit(UUID userId, BigDecimal amount);

    @Modifying
    @Query("""
    UPDATE WalletEntity w
    SET w.balance = w.balance + :amount
    WHERE w.user.userId = :userId
    """)
    int credit(UUID userId, BigDecimal amount);



}
