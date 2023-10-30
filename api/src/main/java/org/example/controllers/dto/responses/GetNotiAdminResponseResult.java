package org.example.controllers.dto.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.example.controllers.dto.DataItem;
import org.example.enumerate.Channel;
import org.example.enumerate.Provider;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class GetNotiAdminResponseResult {
    @JsonProperty("id")
    private int id;
    @JsonProperty("user_id")
    private int userId;
    @JsonProperty("template_id")
    private String templateId;
    @JsonProperty("sender_id")
    private String senderId;
    @JsonProperty("channel")
    private Channel channel;
    @JsonProperty("provider")
    private Provider provider;
    @JsonProperty("is_read")
    private boolean isRead;
    @JsonProperty("data")
    private List<DataItem> data;
    @JsonProperty("service_source")
    private String serviceSource;
    @JsonProperty("create_at")
    private LocalDateTime createAt;
    @JsonProperty("end_at")
    private LocalDateTime endAt;
}
