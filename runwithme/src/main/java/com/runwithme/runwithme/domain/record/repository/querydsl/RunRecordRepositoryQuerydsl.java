package com.runwithme.runwithme.domain.record.repository.querydsl;

import com.runwithme.runwithme.domain.record.dto.CoordinateDto;

import java.util.List;

public interface RunRecordRepositoryQuerydsl {
    int[] coordinatesInsertBatch(Long recordSeq, List<CoordinateDto> coordinates);
}
