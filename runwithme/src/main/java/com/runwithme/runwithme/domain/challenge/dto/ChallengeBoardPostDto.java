package com.runwithme.runwithme.domain.challenge.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@Builder
public class ChallengeBoardPostDto {
    private String challengeBoardContent;
    private MultipartFile image;
}
