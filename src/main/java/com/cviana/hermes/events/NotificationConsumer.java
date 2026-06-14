package com.cviana.hermes.events;

import java.io.IOException;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.cviana.hermes.configurations.RabbitMqConfig;
import com.cviana.hermes.constants.NotificationStatus;
import com.cviana.hermes.notifications.Notification;
import com.cviana.hermes.notifications.NotificationService;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@AllArgsConstructor
@Slf4j
public class NotificationConsumer {

    private NotificationService notificationService;
    private final RedisTemplate<String, String> redisTemplate;
    private ObjectMapper mapper;

    @RabbitListener(queues = RabbitMqConfig.QUEUE_NAME)
	public void processMessage(Message message) throws StreamReadException, DatabindException, IOException  {
        Notification notification = mapper.readValue(message.getBody(), Notification.class);
        try {
            notificationService.dispatch(notification);
            notificationService.updateStatus(notification.getId(), NotificationStatus.COMPLETED);
        } catch (Exception e) {
            redisTemplate.delete(message.getMessageProperties().getHeader("idempotencyKey").toString());
            notificationService.updateStatus(notification.getId(), NotificationStatus.FAILED);
            throw e;
        }
	}

}
