package org.example.services.adapters;

import org.example.models.Notification;
import org.springframework.stereotype.Component;

@Component
public interface ProviderAdapter {
    ProviderResponse sendNotiMultiDevices(String config, Notification notification);
}
