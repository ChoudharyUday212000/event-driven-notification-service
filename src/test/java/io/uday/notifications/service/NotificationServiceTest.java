package io.uday.notifications.service;

import io.uday.notifications.model.DeliveryChannel;
import io.uday.notifications.model.NotificationAccepted;
import io.uday.notifications.model.NotificationEvent;
import io.uday.notifications.model.NotificationRequest;
import io.uday.notifications.publisher.NotificationPublisher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationPublisher publisher;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    void publishesAnEventAndReturnsAcceptedReceipt() {
        NotificationRequest request = new NotificationRequest(
                "order-1001", DeliveryChannel.EMAIL, "demo@example.com",
                "Payment received", "Your payment was received.");

        NotificationAccepted receipt = notificationService.submit(request);

        assertNotNull(receipt.notificationId());
        assertEquals("order-1001", receipt.requestId());
        assertEquals("QUEUED", receipt.status());

        ArgumentCaptor<NotificationEvent> eventCaptor = ArgumentCaptor.forClass(NotificationEvent.class);
        verify(publisher).publish(eventCaptor.capture());
        NotificationEvent event = eventCaptor.getValue();
        assertEquals(receipt.notificationId(), event.notificationId());
        assertEquals(request.requestId(), event.requestId());
        assertEquals(request.channel(), event.channel());
        assertEquals(request.recipient(), event.recipient());
    }

    @Test
    void includesRequestCorrelationIdOnEvent() {
        NotificationRequest request = new NotificationRequest(
                "correlation-22", DeliveryChannel.PUSH, "demo-device-token",
                "Welcome", "Welcome to the demo.");

        notificationService.submit(request);

        verify(publisher).publish(argThat(event -> "correlation-22".equals(event.requestId())));
    }
}
