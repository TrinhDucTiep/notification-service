package org.example.repositories.dto;

import org.example.models.Channel;
import org.example.models.Status;

import java.time.LocalDateTime;

public interface NotificationDetail {
    Integer getNotificationId();
    Integer getUserId();
    Integer getTemplateId();
    Integer getSenderId();

    Status getStatus();

    Boolean getIsRead();

    String getData();

    String getServiceSource();

    LocalDateTime getCreateAt();

    LocalDateTime getEndAt();

    Channel getChannel();

    String getForm();

    String getTitle();

    String getConfig();

    String getProvider();

}
