package com.cviana.hermes.infrastructure.provider;

import java.util.List;

import org.springframework.stereotype.Component;

import com.cviana.hermes.constants.NotificationType;
import com.cviana.hermes.domain.provider.NotificationProvider;

@Component
public class SmsNotificationProvider implements NotificationProvider {

    @Override
    public void send(String[] target, String message) {
        String adressee = List.of(target).toString();
        // if (Math.random() < 0.7) {
        //     System.out.println("[SMS] Simulated failure when sending notification to " + adressee);
        //     throw new RuntimeException("SMS Gateway is temporarily out of service.");
        // }
        System.out.println("[SMS] Enviando para " + adressee + ": " + message);
    }

    @Override
    public boolean supports(NotificationType type) {
        return NotificationType.SMS.equals(type);
    }

}
