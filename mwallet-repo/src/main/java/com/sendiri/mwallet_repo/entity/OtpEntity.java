package com.sendiri.mwallet_repo.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Table(name = "otp")
@ToString
@RequiredArgsConstructor
@Entity
public class OtpEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "otp_id")
    private UUID otpId;

    @Column(name = "phone_no", length = 13)
    private String phoneNo;

    @Column(name = "type")
    private String type;

    public UUID getOtpId() {
        return otpId;
    }

    public void setOtpId(UUID otpId) {
        this.otpId = otpId;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getUsed() {
        return isUsed;
    }

    public void setUsed(Boolean used) {
        isUsed = used;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUsedAt() {
        return usedAt;
    }

    public void setUsedAt(Date usedAt) {
        this.usedAt = usedAt;
    }

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
