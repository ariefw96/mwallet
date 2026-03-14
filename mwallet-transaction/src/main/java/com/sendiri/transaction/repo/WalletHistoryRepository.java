package com.sendiri.transaction.repo;

import com.sendiri.repo.entity.WalletEntity;
import com.sendiri.repo.entity.WalletHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface WalletHistoryRepository extends JpaRepository<WalletHistoryEntity, UUID> {

    List<WalletHistoryEntity> findAllByWallet(WalletEntity wallet);

}
