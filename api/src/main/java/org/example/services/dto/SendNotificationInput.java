package org.example.services.dto;

import lombok.Builder;
import lombok.Data;
import org.example.controllers.dto.requests.ToItem;

import java.util.List;

@Data
@Builder
public class SendNotificationInput {
    private String templateId;
    private String serviceSource;
    private List<ToItem> to;
}
