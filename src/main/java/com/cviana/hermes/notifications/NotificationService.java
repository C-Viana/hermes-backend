package com.cviana.hermes.notifications;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cviana.hermes.constants.NotificationType;
import com.cviana.hermes.domain.provider.NotificationProvider;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class NotificationService {

    // private NotificationRepository notificationRepository;

    private final List<NotificationProvider> providers;

    public void dispatch(NotificationType type, String[] target, String message){
        NotificationProvider provider = providers.stream()
            .filter(p -> p.supports(type))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Provedor não suportado para o tipo: " + type));
        provider.send(target, message);
    }
}
