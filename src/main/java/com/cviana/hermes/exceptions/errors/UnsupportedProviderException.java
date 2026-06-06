package com.cviana.hermes.exceptions.errors;

import com.cviana.hermes.exceptions.messages.HermesExceptionMessages;

public class UnsupportedProviderException extends RuntimeException {

    public UnsupportedProviderException() {
        super(HermesExceptionMessages.UNSUPPORTED_PROVIDER);
    }

    public UnsupportedProviderException(String message) {
        super(message);
    }
}
