package com.runwithme.runwithme.domain.user.dto;

import java.time.LocalDateTime;

public record RefreshTokenDto(
    String tokenName,
    String userEmail,
    LocalDateTime expiredDatetime
) {
}
