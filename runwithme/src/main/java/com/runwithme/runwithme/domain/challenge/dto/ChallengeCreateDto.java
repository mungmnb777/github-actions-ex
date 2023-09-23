package com.runwithme.runwithme.domain.challenge.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
public class ChallengeCreateDto {

    private String name;
    private String description;
    private Long goalDays;
    private String goalType;
    private Long goalAmount;

    private LocalDate timeStart;
    private LocalDate timeEnd;

    private String password;
    private Long cost;

    private Long maxMember;

    private MultipartFile image;
}
