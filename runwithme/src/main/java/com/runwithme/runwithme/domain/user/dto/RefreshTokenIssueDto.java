package com.runwithme.runwithme.domain.user.dto;

public record RefreshTokenIssueDto(
    String grantType,
    String refreshToken
) {
}
