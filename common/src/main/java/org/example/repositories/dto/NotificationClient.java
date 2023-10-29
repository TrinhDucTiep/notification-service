package org.example.repositories.dto;

import java.time.LocalDateTime;

public interface NotificationClient {
    String getTemplateId();
    String getData();
    Boolean getIsRead();
    LocalDateTime getEndAt();
}
