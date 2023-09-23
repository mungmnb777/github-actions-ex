package com.runwithme.runwithme.global.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class PagingResultDto<T> {
//    private int page;
//    private int totalPage;
    private List<T> result;
}
