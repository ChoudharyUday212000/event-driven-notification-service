package io.uday.notifications.model;

import java.time.Instant;
import java.util.UUID;

public record NotificationAccepted(
        UUID notificationId,
        String requestId,
        String status,
        Instant queuedAt) {
}
