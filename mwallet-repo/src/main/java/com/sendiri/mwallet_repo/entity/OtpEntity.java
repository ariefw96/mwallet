package com.sendiri.mwallet_repo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Table(name = "otp")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
public class OtpEntity {

    @Id
    @Column(name = "otp_id")
    private UUID otpId;

    @Column(name = "phone_no", length = 13)
    private String phoneNo;

    @Column(name = "type")
    private String type;

    @Column(name = "code")
    private String code;

    @Column(name = "is_used")
    private Boolean isUsed = false;

    @Column(name = "created_at")
    private Date createdAt = new Date();

    @Column(name = "used_at")
    private Date usedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        OtpEntity otpEntity = (OtpEntity) o;
        return otpId != null && Objects.equals(otpId, otpEntity.otpId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
