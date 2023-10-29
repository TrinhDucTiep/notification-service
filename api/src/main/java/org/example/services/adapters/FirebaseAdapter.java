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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//@RequiredArgsConstructor
@Slf4j
@AllArgsConstructor
@Component("FIREBASE")
public class FirebaseAdapter implements ProviderAdapter {

    private final DeviceRepository deviceRepository;

    @Override
    public ProviderResponse sendNotiMultiUsers(ToItem toItem, Template template) {

        // get firebaseMessaging instance
        FirebaseMessaging myFirebaseMessaging = getFirebaseMessagingInstance(template.getSender().getConfig());

        // send message for each of user
        boolean isSuccess = false;

        // convert form + data => for user
        String title = TemplateConverter.convertTemplate(template.getTitle(), toItem.getData());
        String body = TemplateConverter.convertTemplate(template.getForm(), toItem.getData());

        // send for each device of user
        JsonObject responseJson = new JsonObject();
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
            try {
                String response = myFirebaseMessaging.send(message); // send to Firebase
                log.info("111111111111111111111111: " + response);
                if (response != null && response.startsWith("projects/")) { // check success
                    isSuccess = true;
                }
                responseJson.addProperty(device.getId(), response);
            } catch (FirebaseMessagingException e) {
                responseJson.addProperty(device.getId(), e.getMessage());
                throw new RuntimeException(e);
            }
        }

        // add to the result
        return new ProviderResponse(new SendResponseResult(toItem.getUserId(), isSuccess? Status.SUCCESS : Status.FAIL), responseJson.toString());
    }


    public FirebaseMessaging getFirebaseMessagingInstance(String config) {
        // init fcm from config got in db
        GoogleCredentials googleCredentials;
        try {
            ByteArrayInputStream credentialsStream = new ByteArrayInputStream(config.getBytes());
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
