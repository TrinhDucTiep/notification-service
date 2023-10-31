package org.example.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.controllers.dto.requests.GetNotificationAdminRequest;
import org.example.controllers.dto.requests.GetNotificationClientRequest;
import org.example.controllers.dto.requests.SendNotificationRequest;
import org.example.response.CommonResponse;
import org.example.services.NotificationService;
import org.example.utils.PageableUtil;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class NotificationController {
    private final NotificationService notificationService;

    @PostMapping("/internal/notification/send")
    public ResponseEntity<CommonResponse<Object>> sendNotification(@RequestBody @Validated SendNotificationRequest request) {
        return ResponseEntity.ok(
                new CommonResponse<>(
                        notificationService.sendNotificationMultiUser(request.toInput())
                )
        );
    }

    @GetMapping("/admin/notification")
    public ResponseEntity<CommonResponse<Object>> getNotificationAdmin(@Valid GetNotificationAdminRequest request) {
        Pageable pageable = PageableUtil.generate(request.getPage(), request.getSize(), "create_at");
        return ResponseEntity.ok(
                new CommonResponse<>(
                        notificationService.getNotificationAdmin(request.toInput(), pageable)
                )
        );
    }

    @GetMapping("/client/notification")
    public ResponseEntity<CommonResponse<Object>> getNotificationClient(@Valid GetNotificationClientRequest request) {
        Pageable pageable = PageableUtil.generate(request.getPage(), request.getSize(), "end_at");
        return ResponseEntity.ok(
                new CommonResponse<>(
                        notificationService.getNotificationClient(request.toInput(), pageable)
                )
        );
    }

}
