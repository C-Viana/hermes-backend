package com.cviana.hermes.notifications.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.cviana.hermes.constants.NotificationType;
import com.cviana.hermes.notifications.Notification;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record NotificationRequestDto(
    @NotEmpty
    @Size(min = 1)
    String[] addressee,

    @NotEmpty
    String message,

    @NotNull
    NotificationType type,

    @Nullable
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
    LocalDateTime dateSchedule
) {
    public String[] sortedAddressee() {
        return List.of(addressee).stream().sorted().toArray(String[]::new);
    }

    public NotificationRequestDto canonical() {
        return new NotificationRequestDto(
            sortedAddressee(),
            message,
            type,
            dateSchedule
        );
    }

    public static NotificationRequestDto convert(Notification entity) {
        return new NotificationRequestDto(
            entity.getAddressee(),
            entity.getMessage(),
            entity.getType(),
            entity.getDateSchedule()
        );
    }
}
