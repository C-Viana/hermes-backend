package com.cviana.hermes.domain.provider;

import com.cviana.hermes.constants.NotificationType;

public interface NotificationProvider {
    void send(String[] target, String message);
    boolean supports(NotificationType type);
}
