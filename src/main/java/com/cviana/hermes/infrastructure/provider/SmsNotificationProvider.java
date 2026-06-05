package com.cviana.hermes.infrastructure.provider;

import org.springframework.stereotype.Component;

import com.cviana.hermes.constants.NotificationType;
import com.cviana.hermes.domain.provider.NotificationProvider;

@Component
public class SmsNotificationProvider implements NotificationProvider {

    @Override
    public void send(String[] target, String message) {
        System.out.println("[SMS] Enviando para " + target + ": " + message);
    }

    @Override
    public boolean supports(NotificationType type) {
        return NotificationType.SMS.equals(type);
    }

}
