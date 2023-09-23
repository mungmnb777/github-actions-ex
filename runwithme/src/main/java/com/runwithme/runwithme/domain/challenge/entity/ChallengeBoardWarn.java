package com.runwithme.runwithme.domain.challenge.entity;

import com.runwithme.runwithme.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChallengeBoardWarn {
    @Id
    @Column(columnDefinition = "INT UNSIGNED")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne
    @JoinColumn(name = "user_seq")
    private User user;

    @ManyToOne
    @JoinColumn(name = "challenge_board_seq")
    private ChallengeBoard challengeBoard;

    @Builder
    public ChallengeBoardWarn(User user, ChallengeBoard challengeBoard) {
        this.user = user;
        this.challengeBoard = challengeBoard;
    }
}
