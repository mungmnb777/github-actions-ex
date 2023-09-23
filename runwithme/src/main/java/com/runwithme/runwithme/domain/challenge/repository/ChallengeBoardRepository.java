package com.runwithme.runwithme.domain.challenge.repository;

import com.runwithme.runwithme.domain.challenge.entity.ChallengeBoard;
import com.runwithme.runwithme.domain.challenge.repository.querydsl.ChallengeBoardRepositoryQuerydsl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChallengeBoardRepository extends JpaRepository<ChallengeBoard, Long>, ChallengeBoardRepositoryQuerydsl {

}
