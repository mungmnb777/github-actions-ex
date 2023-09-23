package com.runwithme.runwithme.global.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(name = "결과 응답 데이터")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResultResponseDto {

    @Schema
    private int code;
    @Schema
    private String message;
    @Schema
    private Object data;

    public ResultResponseDto(ResultCode resultCode, Object data) {
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
        this.data = data;
    }

    public ResultResponseDto(ResultCode resultCode, String message, Object data) {
        this.code = resultCode.getCode();
        this.message = message;
        this.data = data;
    }

    //전송할 데이터가 있는 경우
    public static ResultResponseDto of(ResultCode resultCode, Object data) {
        return new ResultResponseDto(resultCode, data);
    }

    //전송할 데이터가 없는 경우
    public static ResultResponseDto of(ResultCode resultCode) {
        return new ResultResponseDto(resultCode, "");
    }

    public static ResultResponseDto of(ResultCode resultCode, String message) {
        return new ResultResponseDto(resultCode, message, null);
    }
}
