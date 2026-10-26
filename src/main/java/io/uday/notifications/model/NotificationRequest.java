package io.uday.notifications.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record NotificationRequest(
        @NotBlank @Size(max = 100) String requestId,
        @NotNull DeliveryChannel channel,
        @NotBlank @Size(max = 320) String recipient,
        @NotBlank @Size(max = 200) String subject,
        @NotBlank @Size(max = 5000) String message) {
}
