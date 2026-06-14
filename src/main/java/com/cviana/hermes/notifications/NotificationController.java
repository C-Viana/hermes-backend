package com.cviana.hermes.notifications;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cviana.hermes.notifications.dto.NotificationRequestDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("api/v1/notifications")
@AllArgsConstructor
public class NotificationController {

    private NotificationService notificationService;

    @PostMapping
    public ResponseEntity<Void> sendNotification( @RequestBody @Valid NotificationRequestDto payload ) throws JsonProcessingException {
        notificationService.create(payload);
        return ResponseEntity.accepted().build();
    }
}
