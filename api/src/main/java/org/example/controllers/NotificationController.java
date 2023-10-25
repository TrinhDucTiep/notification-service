package org.example.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.controllers.dto.requests.GetNotificationAdminRequest;
import org.example.controllers.dto.requests.GetNotificationClientRequest;
import org.example.controllers.dto.requests.SendNotificationRequest;
import org.example.repositories.NotificationRepository;
import org.example.response.CommonResponse;
import org.example.services.NotificationService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class NotificationController {
    private final NotificationService notificationService;
    private final NotificationRepository notificationRepository;

    @PostMapping("/internal/notification/send")
    public ResponseEntity<CommonResponse<Object>> sendNotification(@RequestBody SendNotificationRequest request) {
        return ResponseEntity.ok(new CommonResponse<>(notificationService.sendNotificationMultiUser(request.toInput())));
    }

    @GetMapping("/admin/notification")
    public ResponseEntity<CommonResponse<Object>> getNotificationAdmin(GetNotificationAdminRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), Sort.by("create_at").descending());
        return ResponseEntity.ok(new CommonResponse<>(notificationService.getNotificationAdmin(request.toInput(), pageable)));
    }

    @GetMapping("/client/notification")
    public ResponseEntity<CommonResponse<Object>> getNotificationClient(GetNotificationClientRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), Sort.by("end_at").descending());
        return ResponseEntity.ok(new CommonResponse<>(notificationService.getNotificationClient(request.toInput(), pageable)));
    }

    @GetMapping("/test")
    public Object test(){
        return notificationRepository.findNotificationDetail(1,1,1);
    }
}
