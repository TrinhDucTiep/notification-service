package org.example.services;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.controllers.dto.DataItem;
import org.example.controllers.dto.requests.ToItem;
import org.example.controllers.dto.responses.GetNotiCustomerReponseResult;
import org.example.controllers.dto.responses.SendResponseResult;
import org.example.models.History;
import org.example.models.Notification;
import org.example.models.Status;
import org.example.models.Template;
import org.example.repositories.*;
import org.example.repositories.dto.NotificationAdmin;
import org.example.repositories.dto.NotificationClient;
import org.example.services.adapters.ProviderAdapter;
import org.example.services.adapters.ProviderResponse;
import org.example.services.dto.requests.GetNotificationAdminInput;
import org.example.services.dto.requests.GetNotificationClientInput;
import org.example.services.dto.requests.SendNotificationInput;
import org.example.utils.TemplateConverter;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    private final TemplateRepository templateRepository;
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final HistoryRepository historyRepository;

    private final ApplicationContext applicationContext;

    private final Gson gson = new Gson();

    public List<SendResponseResult> sendNotificationMultiUser(SendNotificationInput request) {
        List<SendResponseResult> sendResponseResults = new ArrayList<>();

        Optional<Template> templateOptional = templateRepository.findById(request.getTemplateId());
        Template template = templateOptional.get();

        // get adapter
        ProviderAdapter adapter = (ProviderAdapter) applicationContext.getBean(template.getSender().getProvider().toString());

        for (ToItem toItem : request.getTo()) {
            // render template

            // save notification with pending status to database
            Notification notification = savePendingNotification(toItem, template, request.getServiceSource());
            // save to history
            saveNotiChangeToHistory(notification, template.getSender().getId(), null);

            // send notification and add result to sendResponseResults
            ProviderResponse providerResponse = adapter.sendNotiMultiUsers(toItem, template);
            sendResponseResults.add(providerResponse.getSendResponseResult());

            // update status for noti
            updateStatusNotification(notification, providerResponse.getSendResponseResult().getStatus());
            // save change to history
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

    public Page<GetNotiCustomerReponseResult> getNotificationClient(GetNotificationClientInput input, Pageable pageable) {
        Page<NotificationClient> pageResult = notificationRepository.getNotificationClient(
                input.getUserId(),
                input.getIsRead(),
                input.getEndAt(),
                pageable);

        return pageResult.map(notificationClient -> {
            Template template = templateRepository.findById(notificationClient.getTemplateId()).get();

            GetNotiCustomerReponseResult responseResult = new GetNotiCustomerReponseResult();
            responseResult.setTitle(TemplateConverter.convertTemplate(template.getTitle(), gson.fromJson(notificationClient.getData(), new TypeToken<List<DataItem>>() {}.getType()) ));
            responseResult.setContent(TemplateConverter.convertTemplate(template.getForm(), gson.fromJson(notificationClient.getData(), new TypeToken<List<DataItem>>() {}.getType()) ));
            responseResult.setRead(notificationClient.getIsRead());
            responseResult.setEndAt(notificationClient.getEndAt());

            return responseResult;
        });
    }

    public Notification savePendingNotification(ToItem toItem, Template template, String serviceSource) {
        // save notification in database with pending state
        org.example.models.Notification notification = org.example.models.Notification.builder()
                .user(userRepository.findById(toItem.getUserId()).get())
                .template(template)
                .status(Status.PENDING)
                .isRead(false)
                .data(gson.toJson(toItem.getData()))
                .serviceSource(serviceSource)
                .createAt(LocalDateTime.now())
                .endAt(null)
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
