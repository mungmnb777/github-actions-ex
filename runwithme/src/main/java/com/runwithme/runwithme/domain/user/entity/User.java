package com.runwithme.runwithme.domain.user.entity;

import com.runwithme.runwithme.domain.user.dto.UserProfileDto;
import com.runwithme.runwithme.global.entity.BaseEntity;
import com.runwithme.runwithme.global.entity.Image;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "user_seq")
    private Long seq;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_seq")
    private Image image;

    @Column(name = "user_role",
            nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Role role;

    @Column(name = "user_email",
            nullable = false,
            unique = true)
    private String email;

    @Column(name = "user_password",
            nullable = false,
            unique = true)
    private String password;

    @Column(name = "user_nickname")
    private String nickname;

    @Column(name = "user_height")
    private int height;

    @Column(name = "user_weight")
    private int weight;

    @Column(name = "user_point",
            nullable = false)
    private int point;

    @Builder
    public User(Long seq, Image image, Role role, String email, String nickname, String password,  int height, int weight, int point) {
        this.seq = seq;
        this.image = image;
        this.role = role;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.height = height;
        this.weight = weight;
        this.point = point;
    }

    public String getRoleValue() {
        return role.getValue();
    }

    public int getRoleStatus() {
        return role.getStatus();
    }


    public void setProfile(UserProfileDto dto) {
        this.nickname = dto.nickname();
        this.height = dto.height();
        this.weight = dto.weight();
        this.role = Role.USER;
    }

    public boolean isTempUser() {
        return role == Role.TEMP_USER;
    }

    public void changeImage(Image image) {
        this.image = image;
    }

    public static User create(OAuth2User oAuth2User) {
        return User.builder()
                .role(Role.TEMP_USER)
                .email(oAuth2User.getName())
                .point(0)
                .build();
    }
}
