package org.example.controllers.dto.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.example.enumerate.Channel;
import org.example.enumerate.EnumValidator;
import org.example.enumerate.Provider;
import org.example.enumerate.Status;
import org.example.services.dto.GetNotificationAdminInput;
import org.springdoc.api.annotations.ParameterObject;

import javax.validation.constraints.Email;
import java.time.LocalDateTime;

@Data
@ParameterObject
public class GetNotificationAdminRequest {
    @JsonProperty("user_id")
    private Integer userId;
    @EnumValidator(enumClass = Channel.class)
    @JsonProperty("channel")
    private Channel channel;
    @Email
    @JsonProperty("email")
    private String email;
    @JsonProperty("phone_number")
    private String phoneNumber;
    @JsonProperty("notification_id")
    private Integer notificationId;
    @EnumValidator(enumClass = Provider.class)
    @JsonProperty("provider")
    private Provider provider;
    @JsonProperty("sender_id")
    private Integer senderId;
    @EnumValidator(enumClass = Status.class)
    @JsonProperty("status")
    private Status status;
    @JsonProperty("create_at")
    private LocalDateTime createAt;
    @JsonProperty("end_at")
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
