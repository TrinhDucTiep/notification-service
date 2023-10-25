package org.example.controllers.dto.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GetNotiCustomerReponseResult {
    @JsonProperty("title")
    private String title;
    @JsonProperty("content")
    private String content;
    @JsonProperty("is_read")
    private boolean isRead;
    @JsonProperty("end_at")
    private LocalDateTime endAt;
}
