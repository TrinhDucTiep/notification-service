package org.example.repositories.dto;

import org.example.enumerate.Channel;
import org.example.enumerate.Status;

import java.time.LocalDateTime;

public interface NotificationDetail {
    Integer getNotificationId();
    Integer getUserId();
    String getTemplateId();
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
