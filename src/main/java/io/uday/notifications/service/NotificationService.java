package io.uday.notifications.service;

import io.uday.notifications.model.NotificationAccepted;
import io.uday.notifications.model.NotificationEvent;
import io.uday.notifications.model.NotificationRequest;
import io.uday.notifications.publisher.NotificationPublisher;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class NotificationService {

    private final NotificationPublisher publisher;

    public NotificationService(NotificationPublisher publisher) {
        this.publisher = publisher;
    }

    public NotificationAccepted submit(NotificationRequest request) {
        Instant now = Instant.now();
        NotificationEvent event = new NotificationEvent(
                UUID.randomUUID(),
                request.requestId(),
                request.channel(),
                request.recipient(),
                request.subject(),
                request.message(),
                now);

        publisher.publish(event);

        return new NotificationAccepted(
                event.notificationId(),
                event.requestId(),
                "QUEUED",
                event.createdAt());
    }
}
