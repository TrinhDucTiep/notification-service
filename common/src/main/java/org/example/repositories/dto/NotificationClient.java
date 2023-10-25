package org.example.repositories.dto;

import java.time.LocalDateTime;

public interface NotificationClient {
    Integer getTemplateId();
    String getData();
    Boolean getIsRead();
    LocalDateTime getEndAt();
}
