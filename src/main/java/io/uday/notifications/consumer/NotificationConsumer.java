package io.uday.notifications.consumer;

import io.uday.notifications.config.NotificationTopics;
import io.uday.notifications.model.NotificationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationConsumer {

    private static final Logger log = LoggerFactory.getLogger(NotificationConsumer.class);
    private final NotificationDeliveryHandler deliveryHandler;

    public NotificationConsumer(NotificationDeliveryHandler deliveryHandler) {
        this.deliveryHandler = deliveryHandler;
    }

    @KafkaListener(topics = NotificationTopics.REQUESTED)
    public void consume(NotificationEvent event) {
        log.debug("Consuming notification event {}", event.notificationId());
        deliveryHandler.deliver(event);
    }
}
