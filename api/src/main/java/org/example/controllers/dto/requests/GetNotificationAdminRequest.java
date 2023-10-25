package org.example.controllers.dto.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import org.example.models.Channel;
import org.example.models.Provider;
import org.example.models.Status;
import org.example.services.dto.requests.GetNotificationAdminInput;
import org.springdoc.api.annotations.ParameterObject;

import java.time.LocalDateTime;

@Data
@ParameterObject
public class GetNotificationAdminRequest {
    @JsonProperty("user_id")
    private Integer userId;
    @JsonProperty("channel")
    private Channel channel;
    @JsonProperty("email")
    private String email;
    @JsonProperty("phone_number")
    private String phoneNumber;
    @JsonProperty("notification_id")
    private Integer notificationId;
    @JsonProperty("provider")
    private Provider provider;
    @JsonProperty("sender_id")
    private Integer senderId;
    @JsonProperty("status")
    private Status status;
    private LocalDateTime createAt;
    private LocalDateTime endAt;

    @JsonProperty("page")
    private Integer page;
    @JsonProperty("size")
    private Integer size;

    public GetNotificationAdminInput toInput() {
        return GetNotificationAdminInput.builder()
                .userId(userId)
                .channel(channel)
                .email(email)
                .phoneNumber(phoneNumber)
                .notificationId(notificationId)
                .provider(provider)
                .senderId(senderId)
                .status(status)
                .createAt(createAt)
                .endAt(endAt)
                .build();
    }
}
