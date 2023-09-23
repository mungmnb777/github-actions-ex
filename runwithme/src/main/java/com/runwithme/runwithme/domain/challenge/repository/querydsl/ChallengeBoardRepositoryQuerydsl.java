package com.runwithme.runwithme.domain.challenge.repository.querydsl;

import com.runwithme.runwithme.domain.challenge.dto.ChallengeBoardResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ChallengeBoardRepositoryQuerydsl {
    Page<ChallengeBoardResponseDto> findAllBoardPage(Long cursorSeq, Long userSeq, Long challengeSeq, Pageable pageable);
}
