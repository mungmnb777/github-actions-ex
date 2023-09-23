package com.runwithme.runwithme.global.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public class BaseEntity {
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createDate;

    @LastModifiedDate
    private LocalDateTime updateDate;

    @CreatedBy
    private String createMember;

    @LastModifiedBy
    private String updateMember;

    @Enumerated(EnumType.STRING)
    private DeleteState isDeleted;

    @PrePersist
    public void prePersist() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        createDate = LocalDateTime.now();
        updateDate = LocalDateTime.now();

        if (authentication != null) {
            createMember = authentication.getName();
            updateMember = authentication.getName();
        }
    }

    @PreUpdate
    public void preUpdate() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        updateDate = LocalDateTime.now();

        if (authentication != null) {
            updateMember = authentication.getName();
        }
    }

    public BaseEntity() {
        this.isDeleted = DeleteState.N;
    }

    public void delete() {
        this.isDeleted = DeleteState.Y;
    }

    public boolean isDeleted() {
        return this.isDeleted.equals(DeleteState.Y);
    }
}
