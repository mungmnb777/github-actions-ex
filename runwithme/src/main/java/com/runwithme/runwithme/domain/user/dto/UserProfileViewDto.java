package com.runwithme.runwithme.domain.user.dto;

import lombok.Builder;

@Builder
public record UserProfileViewDto(Long seq, String email, String nickname, int height, int weight, int point) {
}
