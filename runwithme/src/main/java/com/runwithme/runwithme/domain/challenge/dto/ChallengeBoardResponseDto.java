package com.runwithme.runwithme.domain.challenge.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChallengeBoardResponseDto {
    private Long boardSeq;
    private Long userSeq;
    private String nickname;
    private Long imgSeq;
    private String content;

    @QueryProjection
    public ChallengeBoardResponseDto(Long boardSeq, Long userSeq, String nickname, Long imgSeq, String content) {
        this.boardSeq = boardSeq;
        this.userSeq = userSeq;
        this.nickname = nickname;
        this.imgSeq = imgSeq;
        this.content = content;
    }
}
