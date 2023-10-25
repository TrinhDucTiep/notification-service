package org.example.services.adapters;

import org.example.controllers.dto.requests.SendNotificationRequest;
import org.example.controllers.dto.responses.SendResponseResult;
import org.example.models.Template;
import org.example.services.dto.requests.SendNotificationInput;

import java.util.List;

public interface ProviderAdapter {
    List<SendResponseResult> sendNotiMultiUsers(SendNotificationInput request, Template template);
}
