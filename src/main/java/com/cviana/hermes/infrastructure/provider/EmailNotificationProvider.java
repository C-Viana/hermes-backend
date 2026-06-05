package com.cviana.hermes.infrastructure.provider;

import java.util.List;

import org.springframework.stereotype.Component;

import com.cviana.hermes.constants.NotificationType;
import com.cviana.hermes.domain.provider.NotificationProvider;

@Component
public class EmailNotificationProvider implements NotificationProvider {

    @Override
    public void send(String[] target, String message) {
        String adressee = List.of(target).toString();
        System.out.println("[EMAIL] Enviando para " + adressee + ": " + message);
    }
    
    @Override
    public boolean supports(NotificationType type) {
        return NotificationType.EMAIL.equals(type);
    }

}
