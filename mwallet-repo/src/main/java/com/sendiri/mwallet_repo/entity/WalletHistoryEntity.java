package com.sendiri.mwallet_repo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Table(name = "wallet_histories")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
public class WalletHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "wallet_history_id")
    private UUID walletHistoryId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id", referencedColumnName = "wallet_id")
    private WalletEntity wallet;

    @Column(name = "type")
    private String type;

    @Column(name = "balance")
    private BigDecimal balance;

    @Column(name = "trx_id")
    private String trxId;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        WalletHistoryEntity that = (WalletHistoryEntity) o;
        return getWalletHistoryId() != null && Objects.equals(getWalletHistoryId(), that.getWalletHistoryId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
