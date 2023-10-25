package org.example.services.adapters;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.controllers.dto.requests.ToItem;
import org.example.controllers.dto.responses.SendResponseResult;
import org.example.models.*;
import org.example.repositories.DeviceRepository;
import org.example.repositories.HistoryRepository;
import org.example.repositories.NotificationRepository;
import org.example.repositories.UserRepository;
import org.example.services.dto.requests.SendNotificationInput;
import org.example.utils.TemplateConverter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//@RequiredArgsConstructor
@Slf4j
@AllArgsConstructor
public class FirebaseAdapter implements ProviderAdapter {
    private NotificationRepository notificationRepository;
    private UserRepository userRepository;
    private DeviceRepository deviceRepository;
    private HistoryRepository historyRepository;

    private final Gson gson = new Gson();

    @Override
    public List<SendResponseResult> sendNotiMultiUsers(SendNotificationInput request, Template template) {
        // result
        List<SendResponseResult> sendResponseResults = new ArrayList<>();

        // get sender
        Sender sender = template.getSender();

        // get firebaseMessaging instance
        FirebaseMessaging myFirebaseMessaging = getFirebaseMessagingInstance(sender);

        // send message for each of user
        for (ToItem toItem : request.getTo()) {
            boolean isSuccess = false;

            // save notification in database with pending state
            org.example.models.Notification notification = org.example.models.Notification.builder()
                    .user(userRepository.findById(toItem.getUserId()).get())
                    .template(template)
                    .status(Status.PENDING)
                    .isRead(false)
                    .data(gson.toJson(toItem.getData()))
                    .serviceSource(request.getServiceSource())
                    .createAt(LocalDateTime.now())
                    .endAt(null)
                    .build();
            notification = notificationRepository.save(notification);

            // save to history table
            History history = History.builder()
                    .notification(notification)
                    .senderId(sender.getId())
                    .status(notification.getStatus())
                    .response(null)
                    .build();
            history = historyRepository.save(history);

            // convert form + data => for user
            String title = TemplateConverter.convertTemplate(template.getTitle(), toItem.getData());
            String body = TemplateConverter.convertTemplate(template.getForm(), toItem.getData());

            // send for each device of user
            for (Device device : deviceRepository.findAllByUserId(toItem.getUserId())) {
                Message message = Message.builder()
                        .setNotification(
                                com.google.firebase.messaging.Notification.builder()
                                        .setTitle(title)
                                        .setBody(body)
                                        .build()
                        )
                        .setToken(device.getId())
                        .build();

                JsonObject jsonObject = new JsonObject();
                try {
                    String response = myFirebaseMessaging.send(message);
                    log.info("111111111111111111111111: " + response);
                    if (response != null && response.startsWith("projects/")) { // check success
                        isSuccess = true;
                    }
                    jsonObject.addProperty(device.getId(), response);
                } catch (FirebaseMessagingException e) {
                    jsonObject.addProperty(device.getId(), e.getMessage());
                    throw new RuntimeException(e);
                } finally {
                    history.setResponse(jsonObject.toString());
                }
            }

            // update ending status
            if (isSuccess) {
                notification.setStatus(Status.SUCCESS);
            } else {
                notification.setStatus(Status.FAIL);
            }
            notification.setEndAt(LocalDateTime.now());
            notificationRepository.save(notification);
            // update history
            history.setStatus(notification.getStatus());
            history.setUpdateAt(notification.getEndAt());
            historyRepository.save(history);

            // add to the result
            sendResponseResults.add(new SendResponseResult(toItem.getUserId(), notification.getStatus()));
        }

        return sendResponseResults;
    }

    public FirebaseMessaging getFirebaseMessagingInstance(Sender sender) {
        // init fcm from config in db
        GoogleCredentials googleCredentials;
        try {
            ByteArrayInputStream credentialsStream = new ByteArrayInputStream(sender.getConfig().getBytes());
            googleCredentials = GoogleCredentials.fromStream(credentialsStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        FirebaseOptions firebaseOptions = FirebaseOptions
                .builder()
                .setCredentials(googleCredentials)
                .build();

        // get instance if not null
        FirebaseApp app = null;
        for (FirebaseApp firebaseApp : FirebaseApp.getApps()) {
            if (firebaseApp.getName().equals("my-notification")) {
                app = firebaseApp;
                break;
            }
        }
        if (app == null)
            app = FirebaseApp.initializeApp(firebaseOptions, "my-notification");

        return FirebaseMessaging.getInstance(app);
    }
}
