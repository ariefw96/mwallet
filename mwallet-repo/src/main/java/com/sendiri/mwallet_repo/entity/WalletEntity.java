package com.sendiri.mwallet_repo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Entity
@Table(name = "wallets")
@RequiredArgsConstructor
public class WalletEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "wallet_id")
    private UUID walletId;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private UserEntity user;

    public void setWalletId(UUID walletId) {
        this.walletId = walletId;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Column(name = "balance")
    private BigDecimal balance = BigDecimal.ZERO;
}
