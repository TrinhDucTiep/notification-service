package org.example.repositories.dto;

import org.example.models.Channel;
import org.example.models.Provider;
import org.example.models.Status;

import java.time.LocalDateTime;

public interface NotificationAdmin {
    Integer getId();
    Integer getUserId();
    String getTemplateId();
    Integer getSenderId();
    Status getStatus();
    Channel getChannel();
    Provider getProvider();
    Boolean getIsRead();
    String getData();
    String getServiceSource();
    LocalDateTime getCreateAt();
    LocalDateTime getEndAt();
    String getRenderedTitle();
    String getRenderedContent();
}
