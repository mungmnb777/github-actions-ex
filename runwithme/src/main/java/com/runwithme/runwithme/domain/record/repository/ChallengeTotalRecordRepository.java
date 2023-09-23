package com.runwithme.runwithme.domain.record.repository;

import com.runwithme.runwithme.domain.record.dto.ChallengeTotalRecordResponseDto;
import com.runwithme.runwithme.domain.record.entity.ChallengeTotalRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeTotalRecordRepository extends JpaRepository<ChallengeTotalRecord, Long> {
    ChallengeTotalRecord findByUserSeqAndChallengeSeq(Long userSeq, Long challengeSeq);
}
