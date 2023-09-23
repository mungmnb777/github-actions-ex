package com.runwithme.runwithme.domain.challenge.repository;

import com.runwithme.runwithme.domain.challenge.entity.ChallengeBoard;
import com.runwithme.runwithme.domain.challenge.entity.ChallengeBoardWarn;
import com.runwithme.runwithme.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeBoardWarnRepository extends JpaRepository<ChallengeBoardWarn, Long> {
    boolean existsByUserAndChallengeBoard(User user, ChallengeBoard challengeBoard);
}
