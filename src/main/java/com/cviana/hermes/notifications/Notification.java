package com.cviana.hermes.notifications;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

import com.cviana.hermes.constants.NotificationStatus;
import com.cviana.hermes.constants.NotificationType;
import com.cviana.hermes.notifications.dto.NotificationRequestDto;
import com.fasterxml.uuid.Generators;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "notifications")
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Getter
    @Setter
    @Id
    @Column(columnDefinition = "UUID")
    private UUID id;

    @Getter
    @Setter
    @Size(min = 1)
    private String[] addressee;

    @Getter
    @Setter
    @NotEmpty
    private String message;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    private NotificationStatus status;

    @Getter
    @Setter
    private LocalDateTime dateSchedule;

    @PrePersist
    protected void onCreate() {
        if(this.id == null)
            this.id = Generators.timeBasedEpochGenerator().generate();
    }

    public static Notification convert(NotificationRequestDto dto) {
        return new Notification(
            null, 
            dto.sortedAddressee(), 
            dto.message(), 
            dto.type(), 
            (dto.dateSchedule().compareTo(LocalDateTime.now()) <= 0) ? NotificationStatus.PENDING : NotificationStatus.SCHEDULED, 
            dto.dateSchedule()
        );
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + Arrays.hashCode(addressee);
        result = prime * result + ((message == null) ? 0 : message.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        result = prime * result + ((status == null) ? 0 : status.hashCode());
        result = prime * result + ((dateSchedule == null) ? 0 : dateSchedule.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Notification other = (Notification) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (!Arrays.equals(addressee, other.addressee))
            return false;
        if (message == null) {
            if (other.message != null)
                return false;
        } else if (!message.equals(other.message))
            return false;
        if (type != other.type)
            return false;
        if (status != other.status)
            return false;
        if (dateSchedule == null) {
            if (other.dateSchedule != null)
                return false;
        } else if (!dateSchedule.equals(other.dateSchedule))
            return false;
        return true;
    }

}
