package com.runwithme.runwithme.domain.record.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ChallengeTotalRecordResponseDto {
    private Long totalTime;
    private Long totalDistance;
    private Long totalCalorie;
    private Long totalLongestTime;
    private Long totalLongestDistance;
    private Long totalAvgSpeed;
}
