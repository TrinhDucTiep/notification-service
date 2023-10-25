package org.example.controllers.dto.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.example.services.dto.requests.GetNotificationClientInput;
import org.springdoc.api.annotations.ParameterObject;

import java.time.LocalDateTime;

@Data
@ParameterObject
public class GetNotificationClientRequest {
    @JsonProperty("user_id")
    private Integer userId;
    @JsonProperty("is_read")
    private Boolean isRead;
    @JsonProperty("end_at")
    private LocalDateTime endAt;

    @JsonProperty("page")
    private Integer page;
    @JsonProperty("size")
    private Integer size;

    public GetNotificationClientInput toInput() {
        return GetNotificationClientInput.builder()
                .userId(userId)
                .isRead(isRead)
                .endAt(endAt)
                .build();
    }
}
