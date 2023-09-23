package com.runwithme.runwithme.domain.challenge.entity;

import com.runwithme.runwithme.domain.user.entity.User;
import com.runwithme.runwithme.global.entity.Image;
import lombok.*;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChallengeBoard {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(columnDefinition = "INT UNSIGNED")
    private Long seq;

    @Column(name = "challenge_seq")
    private Long challengeSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq")
    private User user;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "image_seq")
    private Image image;

    @Column(name = "challenge_board_content", length = 255)
    private String challengeBoardContent;

    @Column(name = "challenge_board_reg_time")
    private LocalDateTime challengeBoardRegTime;

    @Builder
    public ChallengeBoard(User user, Long challengeSeq, Image image, String challengeBoardContent, LocalDateTime challengeBoardRegTime){
        this.user = user;
        this.challengeSeq = challengeSeq;
        this.image = image;
        this.challengeBoardContent = challengeBoardContent;
        this.challengeBoardRegTime = challengeBoardRegTime;
    }
}
