package com.runwithme.runwithme.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema
public record UserProfileDto(
        @Schema
        String nickname,
        @Schema
        int height,
        @Schema
        int weight
) {
}