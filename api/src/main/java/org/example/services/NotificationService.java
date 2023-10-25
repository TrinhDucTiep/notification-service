package org.example.services;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.controllers.dto.DataItem;
import org.example.controllers.dto.responses.GetNotiCustomerReponseResult;
import org.example.controllers.dto.responses.SendResponseResult;
import org.example.models.Provider;
import org.example.models.Template;
import org.example.repositories.*;
import org.example.repositories.dto.NotificationAdmin;
import org.example.repositories.dto.NotificationClient;
import org.example.services.adapters.FirebaseAdapter;
import org.example.services.dto.requests.GetNotificationAdminInput;
import org.example.services.dto.requests.GetNotificationClientInput;
import org.example.services.dto.requests.SendNotificationInput;
import org.example.utils.TemplateConverter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
    private final DeviceRepository deviceRepository;
    private final HistoryRepository historyRepository;

    private final Gson gson = new Gson();

    public List<SendResponseResult> sendNotificationMultiUser(SendNotificationInput request) {
        // get template & sender

        Optional<Template> templateOptional = templateRepository.findById(request.getTemplateId());
        Template template = templateOptional.get();

        if (template.getSender().getProvider() == Provider.FIREBASE) {
            return new FirebaseAdapter(
                    notificationRepository,
                    userRepository,
                    deviceRepository,
                    historyRepository
            ).sendNotiMultiUsers(request, template);
        }

        return new ArrayList<>();
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

}
