package io.uday.notifications.publisher;

import io.uday.notifications.model.NotificationEvent;

public interface NotificationPublisher {
    void publish(NotificationEvent event);
}
