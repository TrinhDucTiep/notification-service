package org.example.services.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.example.enumerate.Channel;
import org.example.enumerate.Provider;
import org.example.enumerate.Status;

import java.time.LocalDateTime;

@Data
@Builder
public class GetNotificationAdminInput {
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
    @JsonProperty("create_at")
    private LocalDateTime createAt;
    @JsonProperty("end_at")
    private LocalDateTime endAt;
}
