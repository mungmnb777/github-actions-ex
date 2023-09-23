package com.runwithme.runwithme.domain.challenge.repository;

import com.runwithme.runwithme.domain.challenge.entity.Challenge;
import com.runwithme.runwithme.domain.challenge.repository.querydsl.ChallengeRepositoryQuerydsl;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeRepository extends JpaRepository<Challenge, Long>, ChallengeRepositoryQuerydsl {
}
