package com.cviana.hermes.notifications;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cviana.hermes.constants.NotificationType;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("api/v1/notifications")
@AllArgsConstructor
public class NotificationController {

    private NotificationService notificationService;

    @PostMapping
    public ResponseEntity<Void> sendNotification(@RequestParam NotificationType type, @RequestParam String[] target, @RequestParam String message) {
        notificationService.dispatch(type, target, message);
        return ResponseEntity.ok().build();
    }
}
