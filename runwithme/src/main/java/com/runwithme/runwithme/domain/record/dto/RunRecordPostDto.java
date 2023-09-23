package com.runwithme.runwithme.domain.record.dto;

import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RunRecordPostDto {
    private String startTime;
    private String endTime;
    private Long runningTime;
    private Long runningDistance;
    private List coordinates;
}
