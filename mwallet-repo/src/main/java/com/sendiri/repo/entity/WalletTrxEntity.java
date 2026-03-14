package com.sendiri.repo.entity;

import com.sendiri.repo.constant.TransferStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Table(name = "wallet_transaction")
@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class WalletTrxEntity {

    @Id
    @Column(name = "wallet_trx_id")
    private UUID walletTrxId;

    @Column(name = "from_user")
    private UUID fromUser;

    @Column(name = "to_user")
    private UUID toUser;

    @Column(name = "balance")
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TransferStatus status = TransferStatus.PENDING;

    @Column(name = "created_at")
    private Date createdAt = new Date();

    @Column(name = "updated_at")
    private Date updatedAt;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        WalletTrxEntity that = (WalletTrxEntity) o;
        return getWalletTrxId() != null && Objects.equals(getWalletTrxId(), that.getWalletTrxId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
