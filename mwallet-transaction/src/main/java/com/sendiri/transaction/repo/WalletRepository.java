package com.sendiri.transaction.repo;

import com.sendiri.repo.entity.UserEntity;
import com.sendiri.repo.entity.WalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
    int debit(@Param("userId") UUID userId, @Param("amount") BigDecimal amount);

    @Modifying
    @Query("""
    UPDATE WalletEntity w
    SET w.balance = w.balance + :amount
    WHERE w.user.userId = :userId
    """)
    int credit(@Param("userId") UUID userId, @Param("amount") BigDecimal amount);



}
