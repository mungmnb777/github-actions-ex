package com.runwithme.runwithme.domain.user.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;

public record UserCreateDto(
        @Pattern(regexp = "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")
        String email,
        @Min(8)
        @Max(16)
        String password,
        @Min(2)
        @Max(8)
        String nickname,
        int height,
        int weight
) {
}
