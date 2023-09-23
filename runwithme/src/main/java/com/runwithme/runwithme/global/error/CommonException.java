package com.runwithme.runwithme.global.error;

import com.runwithme.runwithme.global.result.ResultCode;

public abstract class CommonException extends RuntimeException {
    public CommonException(String message) {
        super(message);
    }

    public abstract ResultCode getResultCode();
}
