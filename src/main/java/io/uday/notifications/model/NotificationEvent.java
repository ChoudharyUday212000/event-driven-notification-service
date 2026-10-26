package io.uday.notifications.model;

import java.time.Instant;
import java.util.UUID;

public record NotificationEvent(
        UUID notificationId,
        String requestId,
        DeliveryChannel channel,
        String recipient,
        String subject,
        String message,
        Instant createdAt) {
}
