package org.example.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CommonResponse<T> {
    @JsonProperty("code")
    private int code;
    @JsonProperty("message")
    private String message;
    @JsonProperty("result")
    private T result;

    public CommonResponse(T result) {
        this.result = result;
    }
}
