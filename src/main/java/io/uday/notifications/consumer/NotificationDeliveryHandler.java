package io.uday.notifications.consumer;

import io.uday.notifications.model.NotificationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class NotificationDeliveryHandler {

    private static final Logger log = LoggerFactory.getLogger(NotificationDeliveryHandler.class);

    /**
     * Demo adapter: logs the consumed event but does not send a real notification.
     * Replace this class with an email/SMS/push provider integration.
     */
    public void deliver(NotificationEvent event) {
        log.info("Demo delivery accepted: notificationId={}, requestId={}, channel={}",
                event.notificationId(), event.requestId(), event.channel());
    }
}
