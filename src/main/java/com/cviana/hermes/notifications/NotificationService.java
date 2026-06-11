package com.cviana.hermes.notifications;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.cviana.hermes.constants.NotificationStatus;
import com.cviana.hermes.domain.provider.NotificationProvider;
import com.cviana.hermes.exceptions.errors.HermesServerErrorException;
import com.cviana.hermes.exceptions.errors.NotificationNotFoundException;
import com.cviana.hermes.exceptions.errors.UnsupportedProviderException;
import com.cviana.hermes.exceptions.messages.HermesExceptionMessages;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class NotificationService {

    private NotificationRepository notificationRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private ObjectMapper mapper;

    private final List<NotificationProvider> providers;
    
    public void fallbackSendNotification(Throwable throwable) {
        log.error("[FALLBACK] All attempts to send notification failed", throwable);
        throw new HermesServerErrorException(HermesExceptionMessages.SERVICE_UNAVAILABLE);
    }

    // @CircuitBreaker(name = "notificationBreaker", fallbackMethod = "fallbackSendNotification")
    @Retry(name = "notificationRetry", fallbackMethod = "fallbackSendNotification")
    public void dispatch(Notification notification){
        try {
            NotificationProvider provider = providers.stream()
                .filter(p -> p.supports(notification.getType()))
                .findFirst()
                .orElseThrow(() -> new UnsupportedProviderException("Provedor não suportado para o tipo: " + notification.getType()));
            provider.send(notification.getAddressee(), notification.getMessage());
        } catch (Exception e) {
            redisTemplate.delete(String.valueOf(notification.hashCode()));
            throw e;
        }
    }

    public Notification create(Notification notification) throws JsonProcessingException {
        String notificationHash = String.valueOf(notification.hashCode());

        if(redisTemplate.opsForValue().get(notificationHash) == null) {
            Notification result = notificationRepository.save(notification);
            if(result != null)
                redisTemplate.opsForValue().setIfAbsent(String.valueOf(result.hashCode()), mapper.writeValueAsString(result), Duration.of(30L, ChronoUnit.MINUTES));
            return result;
        }
        else {
            log.info("[REDIS] Cache hit for notification {}", notificationHash);
            return null;
        }
    }

    public Notification updateStatus(UUID id, NotificationStatus status) {
        Notification result = notificationRepository.findById(id).orElseThrow(() -> new NotificationNotFoundException());
        result.setStatus(status);
        return notificationRepository.save(result);
    }
}
