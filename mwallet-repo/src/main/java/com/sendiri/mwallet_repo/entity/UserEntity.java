package com.sendiri.mwallet_repo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Table(name = "users")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
public class UserEntity {

    @Id
    @Column(name = "user_id", unique = true)
    private UUID userId;

    @Column(name = "phone_no", length = 13)
    private String phoneNo;

    @JsonIgnore
    @Column(name = "pin")
    private String pin;

    @Column(name = "is_active")
    private Boolean isActive = false;

    @Column(name = "is_verified")
    private Boolean isVerified = false;

    @Column(name = "created_at")
    private Date createdAt = new Date();

    @Column(name = "updated_at")
    private Date updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserEntity that = (UserEntity) o;
        return userId != null && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
