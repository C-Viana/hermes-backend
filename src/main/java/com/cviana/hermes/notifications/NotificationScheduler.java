package com.cviana.hermes.notifications;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.cviana.hermes.configurations.RabbitMqConfig;
import com.cviana.hermes.constants.NotificationStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@AllArgsConstructor
@Slf4j
public class NotificationScheduler {

    private final NotificationRepository repository;
    private final ObjectMapper mapper;
    private final RabbitTemplate rabbit;

    @Scheduled(fixedDelay = 30000) // verifica a cada 30 segundos
    public void dispatchScheduledNotifications() throws JsonProcessingException {
        List<Notification> ready = repository.findScheduledReady(LocalDateTime.now());
        log.info("[SCHEDULER] {} notificações prontas para envio", ready.size());

        for (Notification notification : ready) {
            notification.setStatus(NotificationStatus.PENDING);
            repository.save(notification);
            rabbit.convertAndSend(
                RabbitMqConfig.EXCHANGE_NAME,
                RabbitMqConfig.ROUTING_KEY,
                mapper.writeValueAsString(notification)
            );
        }
    }
}
