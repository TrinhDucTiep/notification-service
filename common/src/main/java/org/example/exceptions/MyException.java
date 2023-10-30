package org.example.exceptions;

import lombok.Getter;
import org.example.response.CommonResponse;
import org.springframework.http.HttpStatus;

@Getter
public class MyException extends RuntimeException {
    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    public MyException(Throwable cause, String code, String message, HttpStatus httpStatus) {
        super(cause);
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public MyException(Throwable cause, ErrorCodeList errorCodeList, HttpStatus httpStatus) {
        super(cause);
        this.code = errorCodeList.toCode();
        this.message = errorCodeList.toString();
        this.httpStatus = httpStatus;
    }

    public CommonResponse<String> toCommonResponse() {
        return new CommonResponse<>(
                this.code,
                this.message,
                null
        );
    }
}
