package com.runwithme.runwithme.global.error;

import com.runwithme.runwithme.global.result.ResultCode;
import lombok.Getter;

@Getter
public class CustomException extends CommonException {
    private final ResultCode resultCode;

    public CustomException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.resultCode = resultCode;
    }

    @Override
    public ResultCode getResultCode() {
        return this.resultCode;
    }
}
