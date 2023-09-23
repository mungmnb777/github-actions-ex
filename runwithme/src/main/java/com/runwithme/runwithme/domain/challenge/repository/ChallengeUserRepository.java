package com.runwithme.runwithme.domain.challenge.repository;

import com.runwithme.runwithme.domain.challenge.entity.ChallengeUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeUserRepository extends JpaRepository<ChallengeUser, Long> {
    boolean existsByUserSeqAndChallengeSeq(Long userSeq, Long challengeSeq);
}
