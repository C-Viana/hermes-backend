package com.cviana.hermes.notifications;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cviana.hermes.configurations.RabbitMqConfig;
import com.cviana.hermes.constants.NotificationStatus;
import com.cviana.hermes.domain.provider.NotificationProvider;
import com.cviana.hermes.exceptions.errors.HermesServerErrorException;
import com.cviana.hermes.exceptions.errors.NotificationNotFoundException;
import com.cviana.hermes.exceptions.errors.UnsupportedProviderException;
import com.cviana.hermes.notifications.dto.NotificationRequestDto;
import com.cviana.hermes.utilities.EncoderSha256;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class NotificationService {

    private NotificationRepository notificationRepository;
    private RabbitTemplate rabbit;
    private final RedisTemplate<String, String> redisTemplate;
    private ObjectMapper mapper;

    private final List<NotificationProvider> providers;
    
    public void fallbackSendNotification(Throwable throwable) {
        log.error("[FALLBACK] All attempts to send notification failed", throwable);
        throw new AmqpRejectAndDontRequeueException("Falha ao processar notificação. Enviando para DLQ.", throwable);
    }

    @Retry(name = "notificationRetry", fallbackMethod = "fallbackSendNotification")
    public void dispatch(Notification notification) throws StreamReadException, DatabindException, IOException {
        NotificationProvider provider = providers.stream()
                .filter(p -> p.supports(notification.getType()))
                .findFirst()
                .orElseThrow(() -> new UnsupportedProviderException("Provedor não suportado para o tipo: " + notification.getType()));
            provider.send(notification.getAddressee(), notification.getMessage());
    }

    public Notification create(NotificationRequestDto payload) throws JsonProcessingException {
        Notification entity = Notification.convert(payload);
        String idempotencyKey = EncoderSha256.encode(mapper.writeValueAsString(payload.canonical()));

        try {
            if(redisTemplate.opsForValue().setIfAbsent(idempotencyKey, "TEMP", Duration.of(10L, ChronoUnit.SECONDS))) {
                Notification result = notificationRepository.save(entity);

                redisTemplate.opsForValue().set(
                    idempotencyKey, 
                    mapper.writeValueAsString(result), 
                    Duration.of(30L, ChronoUnit.MINUTES)
                );

                if (payload.dateSchedule() == null || payload.dateSchedule().compareTo(LocalDateTime.now()) <= 0)
                    rabbit.convertAndSend(
                        RabbitMqConfig.EXCHANGE_NAME, 
                        RabbitMqConfig.ROUTING_KEY, 
                        mapper.writeValueAsString(result),
                        message -> {
                            message.getMessageProperties().setHeader("idempotencyKey", idempotencyKey);
                            return message;
                        }
                    );
                return result;
            }
            else {
                log.info("[REDIS] Cache hit for notification {}", idempotencyKey);
                return null;
            }
        }
        catch (Exception e) {
            redisTemplate.delete(idempotencyKey);
            throw new HermesServerErrorException(e.getMessage());
        }
        
    }

    public Notification updateStatus(UUID id, NotificationStatus status) {
        Notification result = notificationRepository.findById(id).orElseThrow(NotificationNotFoundException::new);
        result.setStatus(status);
        return notificationRepository.save(result);
    }

    public Notification getById(UUID id) {
        return notificationRepository.findById(id).orElseThrow(NotificationNotFoundException::new);
    }

}
