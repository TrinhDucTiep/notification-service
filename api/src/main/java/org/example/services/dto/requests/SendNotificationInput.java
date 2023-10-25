package org.example.services.dto.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.example.controllers.dto.requests.ToItem;

import java.util.List;

@Data
@Builder
public class SendNotificationInput {
    private Integer templateId;
    private String serviceSource;
    private List<ToItem> to;
}
