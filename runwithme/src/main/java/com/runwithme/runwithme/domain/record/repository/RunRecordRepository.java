package com.runwithme.runwithme.domain.record.repository;

import com.runwithme.runwithme.domain.record.entity.RunRecord;
import com.runwithme.runwithme.domain.record.repository.querydsl.RunRecordRepositoryQuerydsl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface RunRecordRepository extends JpaRepository<RunRecord, Long>, RunRecordRepositoryQuerydsl {

    boolean existsByUserSeqAndChallengeSeqAndRegTime(Long userSeq, Long challengeSeq, LocalDate regTime);
    List<RunRecord> findAllByChallengeSeq(Long challengeSeq);
    List<RunRecord> findAllByUserSeqAndChallengeSeq(Long userSeq, Long challengeSeq);
}
