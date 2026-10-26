package io.uday.notifications.publisher;

public class NotificationPublishException extends RuntimeException {
    public NotificationPublishException(String message, Throwable cause) {
        super(message, cause);
    }
}
