package com.cviana.hermes.exceptions.errors;

import com.cviana.hermes.exceptions.messages.HermesExceptionMessages;

public class NotificationNotFoundException extends RuntimeException {

    public NotificationNotFoundException() {
        super(HermesExceptionMessages.NOTIFICATION_NOT_FOUND);
    }

    public NotificationNotFoundException(String message) {
        super(message);
    }
}
