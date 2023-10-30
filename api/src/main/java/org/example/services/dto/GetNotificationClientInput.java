package org.example.services.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class GetNotificationClientInput {
    @JsonProperty("user_id")
    private Integer userId;
    @JsonProperty("is_read")
    private Boolean isRead;
    @JsonProperty("end_at")
    private LocalDateTime endAt;
}
