package org.example.services.adapters;

import org.example.controllers.dto.requests.SendNotificationRequest;
import org.example.controllers.dto.requests.ToItem;
import org.example.controllers.dto.responses.SendResponseResult;
import org.example.models.Template;
import org.example.services.dto.requests.SendNotificationInput;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ProviderAdapter {
    ProviderResponse sendNotiMultiUsers(ToItem toItem, Template template);
}
