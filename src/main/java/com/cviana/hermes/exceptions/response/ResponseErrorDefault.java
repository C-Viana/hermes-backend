package com.cviana.hermes.exceptions.response;

import java.time.LocalDateTime;

public record ResponseErrorDefault(
    int code,
    String message,
    LocalDateTime timestamp
) {
    
}
