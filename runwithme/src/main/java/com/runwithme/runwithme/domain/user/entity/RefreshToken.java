package com.runwithme.runwithme.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

    @Id
    @Column(name = "rt_name")
    private String name;

    @Column(name = "rt_user_email")
    private String userEmail;

    @Column(name = "rt_expired_datetime")
    private LocalDateTime expiredDateTime;

    @Builder
    public RefreshToken(String name, String userEmail, LocalDateTime expiredDateTime) {
        this.name = name;
        this.userEmail = userEmail;
        this.expiredDateTime = expiredDateTime;
    }
}
