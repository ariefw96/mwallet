package com.sendiri.transaction.repo;

import com.sendiri.repo.entity.WalletTrxEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WalletTrxRepository extends JpaRepository<WalletTrxEntity, UUID> {
}
