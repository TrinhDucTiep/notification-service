package org.example.services.adapters;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.controllers.dto.responses.SendResponseResult;
import org.example.models.Device;
import org.example.models.Notification;
import org.example.enumerate.Status;
import org.example.repositories.DeviceRepository;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Slf4j
@AllArgsConstructor
@Component("FIREBASE")
public class FirebaseAdapter implements ProviderAdapter {

    private final DeviceRepository deviceRepository;

    @Override
    public ProviderResponse sendNotiMultiDevices(String config, Notification notification) {

        // get firebaseMessaging instance
        FirebaseMessaging myFirebaseMessaging = getFirebaseMessagingInstance(config);

        // send message for each of user
        boolean isSuccess = false;

        // send for each device of user
        JsonObject responseJson = new JsonObject();
        for (Device device : deviceRepository.findAllByUserId(notification.getUser().getId())) {
            Message message = Message.builder()
                    .setNotification(
                            com.google.firebase.messaging.Notification.builder()
                                    .setTitle(notification.getRenderedTitle())
                                    .setBody(notification.getRenderedContent())
                                    .build()
                    )
                    .setToken(device.getId())
                    .build();
            try {
                log.info("Start sending notification by FirebaseMessaging to device with id: {}", device.getId());
                String response = myFirebaseMessaging.send(message); // send to Firebase

                if (response != null && response.startsWith("projects/")) { // check success
                    log.info("Sent notification by FirebaseMessaging to device with id: {} - status: Success", device.getId());
                    isSuccess = true;
                } else {
                    log.info("Sent notification by FirebaseMessaging to device with id: {} - status: Fail", device.getId());
                }
                responseJson.addProperty(device.getId(), response);
            } catch (FirebaseMessagingException e) {
                log.info("Sent notification by FirebaseMessaging to device with id: {} - status: Fail - error: {}", device.getId(), e.getMessage());
                responseJson.addProperty(device.getId(), e.getMessage());
                throw new RuntimeException(e);
            }
        }

        // add to the result
        return new ProviderResponse(new SendResponseResult(notification.getUser().getId(), isSuccess? Status.SUCCESS : Status.FAIL), responseJson.toString());
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
