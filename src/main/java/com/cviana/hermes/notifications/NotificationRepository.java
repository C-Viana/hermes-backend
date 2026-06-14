package com.cviana.hermes.notifications;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {

    @Query("SELECT n FROM Notification n WHERE n.status = 'SCHEDULED' AND n.dateSchedule <= :now")
    List<Notification> findScheduledReady(@Param("now") LocalDateTime now);
}
