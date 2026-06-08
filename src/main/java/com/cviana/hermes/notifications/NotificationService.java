package com.cviana.hermes.notifications;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cviana.hermes.constants.NotificationType;
import com.cviana.hermes.domain.provider.NotificationProvider;
import com.cviana.hermes.exceptions.errors.HermesServerErrorException;
import com.cviana.hermes.exceptions.errors.UnsupportedProviderException;
import com.cviana.hermes.exceptions.messages.HermesExceptionMessages;

import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class NotificationService {

    private NotificationRepository notificationRepository;

    private final List<NotificationProvider> providers;
    
    public void fallbackSendNotification(Throwable throwable) {
        log.error("[FALLBACK] All attempts to send notification failed", throwable);
        throw new HermesServerErrorException(HermesExceptionMessages.SERVICE_UNAVAILABLE);
    }

    // @CircuitBreaker(name = "notificationBreaker", fallbackMethod = "fallbackSendNotification")
    @Retry(name = "notificationRetry", fallbackMethod = "fallbackSendNotification")
    public void dispatch(NotificationType type, String[] target, String message){
        NotificationProvider provider = providers.stream()
            .filter(p -> p.supports(type))
            .findFirst()
            .orElseThrow(() -> new UnsupportedProviderException("Provedor não suportado para o tipo: " + type));
        provider.send(target, message);
    }

    public Notification save(Notification notification) {
        return notificationRepository.save(notification);
    }
}
