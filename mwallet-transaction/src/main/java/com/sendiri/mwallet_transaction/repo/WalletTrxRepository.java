package com.sendiri.mwallet_transaction.repo;

import com.sendiri.mwallet_repo.entity.WalletTrxEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WalletTrxRepository extends JpaRepository<WalletTrxEntity, UUID> {
}
