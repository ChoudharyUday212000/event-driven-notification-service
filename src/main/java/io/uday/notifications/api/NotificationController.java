package io.uday.notifications.api;

import io.uday.notifications.model.NotificationAccepted;
import io.uday.notifications.model.NotificationRequest;
import io.uday.notifications.service.NotificationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    public ResponseEntity<NotificationAccepted> submit(
            @Valid @RequestBody NotificationRequest request) {
        NotificationAccepted accepted = notificationService.submit(request);
        return ResponseEntity.accepted()
                .location(URI.create("/api/v1/notifications/" + accepted.notificationId()))
                .body(accepted);
    }
}
