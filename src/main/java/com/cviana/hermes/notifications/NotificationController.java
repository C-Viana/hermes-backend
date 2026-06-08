package com.cviana.hermes.notifications;

import java.time.LocalDateTime;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cviana.hermes.configurations.RabbitMqConfig;
import com.cviana.hermes.constants.NotificationStatus;
import com.cviana.hermes.constants.NotificationType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("api/v1/notifications")
@AllArgsConstructor
@Valid
public class NotificationController {

    private NotificationService notificationService;
    private RabbitTemplate rabbit;

    @PostMapping
    public ResponseEntity<Void> sendNotification(
        @RequestParam NotificationType type, 
        @RequestParam String[] addressee, 
        @RequestParam String message, 
        @RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-YYYY HH:mm:ss") @Nullable LocalDateTime dateSchedule
    ) throws JsonProcessingException {
        Notification entity = new Notification(null, addressee, message, type, NotificationStatus.PENDING, dateSchedule);
        notificationService.save(entity);
        
        rabbit.convertAndSend(RabbitMqConfig.EXCHANGE_NAME, RabbitMqConfig.ROUTING_KEY, new ObjectMapper().writeValueAsString(entity));

        return ResponseEntity.accepted().build();
    }
}
