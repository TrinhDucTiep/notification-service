package org.example.services;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.controllers.dto.requests.ToItem;
import org.example.controllers.dto.responses.SendResponseResult;
import org.example.enumerate.Status;
import org.example.exceptions.MyException;
import org.example.models.*;
import org.example.repositories.HistoryRepository;
import org.example.repositories.NotificationRepository;
import org.example.repositories.TemplateRepository;
import org.example.repositories.UserRepository;
import org.example.repositories.dto.NotificationAdmin;
import org.example.repositories.dto.NotificationClient;
import org.example.services.adapters.ProviderAdapter;
import org.example.services.adapters.ProviderResponse;
import org.example.services.dto.GetNotificationAdminInput;
import org.example.services.dto.GetNotificationClientInput;
import org.example.services.dto.SendNotificationInput;
import org.example.utils.TemplateConverter;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    private final TemplateRepository templateRepository;
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final HistoryRepository historyRepository;

    private final ApplicationContext applicationContext;

    private final TemplateConverter templateConverter;

    private final Gson gson = new Gson();

    public List<SendResponseResult> sendNotificationMultiUser(SendNotificationInput request) {
        List<SendResponseResult> sendResponseResults = new ArrayList<>();

        // get template
        Template template = templateRepository.findById(request.getTemplateId()).orElseThrow(
                () -> new MyException(
                        null,
                        "TEMPLATE_001",
                        "Template with id (" + request.getTemplateId() + ") not found",
                        HttpStatus.BAD_REQUEST
                )
        );

        // get adapter
        ProviderAdapter adapter = (ProviderAdapter) applicationContext.getBean(template.getSender().getProvider().toString());

        // extract arguments from thymeleaf
        Set<String> templateVariables = TemplateConverter.extractVariables(template.getTitle(), template.getForm());

        for (ToItem toItem : request.getTo()) {
            // render template
            if (!TemplateConverter.isDataValid(toItem.getData(), templateVariables))
                throw new MyException(
                        null,
                        "TEMPLATE_002",
                        "Template arguments wrong",
                        HttpStatus.BAD_REQUEST
                );
            String renderedTitle = templateConverter.convertTemplate(template.getTitle(), toItem.getData());
            String renderedContent = templateConverter.convertTemplate(template.getForm(), toItem.getData());

            // save pending notification and it's history to database
            Notification notification = savePendingNotification(toItem, template, request.getServiceSource(), renderedTitle, renderedContent);
            saveNotiChangeToHistory(notification, template.getSender().getId(), null);

            // send notification and add result to sendResponseResults
            ProviderResponse providerResponse = adapter.sendNotiMultiDevices(template.getSender().getConfig(), notification);
            sendResponseResults.add(providerResponse.getSendResponseResult());

            // update status for notification and history of changes
            updateStatusNotification(notification, providerResponse.getSendResponseResult().getStatus());
            saveNotiChangeToHistory(notification, template.getSender().getId(), providerResponse.getResponse());
        }

        return sendResponseResults;
    }

    public Page<NotificationAdmin> getNotificationAdmin(GetNotificationAdminInput input, Pageable pageable) {
        return notificationRepository.getNotificationAdmin(
                input.getUserId(),
                input.getChannel(),
                input.getEmail(),
                input.getPhoneNumber(),
                input.getNotificationId(),
                input.getProvider(),
                input.getSenderId(),
                input.getStatus(),
                input.getCreateAt(),
                input.getEndAt(),
                pageable
        );
    }

    public Page<NotificationClient> getNotificationClient(GetNotificationClientInput input, Pageable pageable) {
        return notificationRepository.getNotificationClient(
            input.getUserId(),
            input.getIsRead(),
            input.getEndAt(),
            pageable);
    }

    public Notification savePendingNotification(ToItem toItem, Template template, String serviceSource, String renderedTitle, String renderedContent) {
        User user = userRepository.findById(toItem.getUserId()).orElseThrow(
                () -> new MyException(
                        null,
                        "USER_001",
                        "User with id (" + toItem.getUserId() + ") not found",
                        HttpStatus.BAD_REQUEST
                )
        );
        // save notification in database with pending state
        org.example.models.Notification notification = org.example.models.Notification.builder()
                .user(user)
                .template(template)
                .status(Status.PENDING)
                .isRead(false)
                .data(gson.toJson(toItem.getData()))
                .serviceSource(serviceSource)
                .createAt(LocalDateTime.now())
                .endAt(null)
                .renderedTitle(renderedTitle)
                .renderedContent(renderedContent)
                .build();
        return notificationRepository.save(notification);
    }

    public History saveNotiChangeToHistory(Notification notification, Integer senderId, String response) {
        History history = History.builder()
                .notification(notification)
                .senderId(senderId)
                .status(notification.getStatus())
                .updateAt((notification.getEndAt() != null) ? notification.getEndAt() : notification.getCreateAt())
                .response(response)
                .build();
        return historyRepository.save(history);
    }

    public Notification updateStatusNotification(Notification notification, Status status) {
        notification.setStatus(status);
        notification.setEndAt(LocalDateTime.now());
        return notificationRepository.save(notification);
    }

}
