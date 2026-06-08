package com.cviana.hermes.events;

import java.io.IOException;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.cviana.hermes.configurations.RabbitMqConfig;
import com.cviana.hermes.notifications.Notification;
import com.cviana.hermes.notifications.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@AllArgsConstructor
@Slf4j
public class NotificationConsumer {

    private NotificationService notificationService;

    @RabbitListener(queues = RabbitMqConfig.QUEUE_NAME)
	public void processMessage(Message message) throws IOException {
        
        Notification notification = new ObjectMapper().readValue(message.getBody(), Notification.class);
        notificationService.dispatch(notification.getType(), notification.getAddressee(), notification.getMessage());
	}

}
