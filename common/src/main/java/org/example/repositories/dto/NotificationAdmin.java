package org.example.repositories.dto;

import org.example.enumerate.Channel;
import org.example.enumerate.Provider;
import org.example.enumerate.Status;

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
