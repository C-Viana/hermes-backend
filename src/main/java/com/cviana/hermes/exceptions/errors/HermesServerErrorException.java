package com.cviana.hermes.exceptions.errors;

import com.cviana.hermes.exceptions.messages.HermesExceptionMessages;

public class HermesServerErrorException extends RuntimeException {

    public HermesServerErrorException() {
        super(HermesExceptionMessages.INTERNAL_SERVER_ERROR);
    }

    public HermesServerErrorException(String message) {
        super(message);
    }
}
