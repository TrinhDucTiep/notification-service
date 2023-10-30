package org.example.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.configurations.Constant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResponse<T> {
    @JsonProperty("code")
    private String code;
    @JsonProperty("message")
    private String message;
    @JsonProperty("result")
    private T result;

    public CommonResponse(T result) {
        this.code = Constant.SUCCESS_CODE;
        this.message = Constant.SUCCESS_MESSAGE;
        this.result = result;
    }

    public static CommonResponse<Object> badRequest(String message) {
        return new CommonResponse<>(
                Constant.BAD_REQUEST_CODE,
                message,
                null
        );
    }

    public static CommonResponse<Object> internalError() {
        return new CommonResponse<>(
                Constant.INTERNAL_SERVER_ERROR_CODE,
                Constant.INTERNAL_SERVER_ERROR_MESSAGE,
                null
        );
    }
}
