package io.uday.notifications.publisher;

import io.uday.notifications.config.NotificationTopics;
import io.uday.notifications.model.NotificationEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class KafkaNotificationPublisher implements NotificationPublisher {

    private final KafkaTemplate<String, NotificationEvent> kafkaTemplate;

    public KafkaNotificationPublisher(KafkaTemplate<String, NotificationEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publish(NotificationEvent event) {
        try {
            // Wait for broker acknowledgement so a failed publish is not called queued.
            kafkaTemplate.send(NotificationTopics.REQUESTED, event.requestId(), event)
                    .get(5, TimeUnit.SECONDS);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new NotificationPublishException("Interrupted while publishing notification", exception);
        } catch (Exception exception) {
            throw new NotificationPublishException("Could not publish notification to Kafka", exception);
        }
    }
}
