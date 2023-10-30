package org.example.repositories.dto;

import java.time.LocalDateTime;

public interface NotificationClient {
    String getRenderedTitle();
    String getRenderedContent();
    Boolean getIsRead();
    LocalDateTime getEndAt();
}
