package org.example.controllers.dto.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.example.services.dto.requests.SendNotificationInput;

import java.util.List;

@Data
public class SendNotificationRequest {
    @JsonProperty("template_id")
    private String templateId;
    @JsonProperty("service_source")
    private String serviceSource;
    @JsonProperty("to")
    private List<ToItem> to;

    public SendNotificationInput toInput() {
        return SendNotificationInput.builder()
                .templateId(templateId)
                .serviceSource(serviceSource)
                .to(to)
                .build();
    }
}

