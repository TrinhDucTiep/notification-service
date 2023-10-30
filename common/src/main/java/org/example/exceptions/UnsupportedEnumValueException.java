package org.example.exceptions;

import org.example.response.CommonResponse;

public class UnsupportedEnumValueException extends RuntimeException{
    private final String value;

    public UnsupportedEnumValueException(String value) {
        this.value = "Unsupported value: " + value;
    }

    public String getValue() {
        return this.value;
    }

    public CommonResponse<Object> toCommonResponse() {
        return new CommonResponse<>(
                "400",
                this.value,
                null
        );
    }
}
