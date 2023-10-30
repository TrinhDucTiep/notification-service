package org.example.controllers.dto.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.example.services.dto.SendNotificationInput;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class SendNotificationRequest {
    @NotNull
    @NotBlank
    @JsonProperty("template_id")
    private String templateId;
    @NotNull
    @NotBlank
    @JsonProperty("service_source")
    private String serviceSource;
    @NotNull
    @Valid
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

