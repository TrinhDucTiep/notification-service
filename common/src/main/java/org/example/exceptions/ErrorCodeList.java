package org.example.exceptions;

public enum ErrorCodeList {
    InternalServerError("500", "Internal Error", true),
    InvalidParameter("400", "Invalid parameter"),
    ;

    private final String code;
    private final String message;
    private Boolean shouldAlert = false;

    ErrorCodeList(String code, String message) {
        this.code = code;
        this.message = message;
    }

    ErrorCodeList(String code, String message, Boolean shouldAlert) {
        this.code = code;
        this.message = message;
        this.shouldAlert = shouldAlert;
    }

    public String toCode() {
        return this.code;
    }

    @Override
    public String toString() {
        return this.message;
    }

    public Boolean shouldAlert() {
        return shouldAlert;
    }
}
