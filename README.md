# Event-Driven Notification Service

A Java 17 and Spring Boot API that publishes notification requests to Kafka, consumes them asynchronously, retries failed deliveries, and routes exhausted messages to a dead-letter topic (DLT).

This is a portfolio/demo implementation. Its delivery handler logs a simulated delivery; it does not send real emails, SMS messages, or push notifications.

## Why this project

It demonstrates backend patterns that matter in event-driven systems: asynchronous processing, message keys, consumer groups, retry policy, dead-letter routing, request validation, broker acknowledgement handling and testable service boundaries.

## Architecture

~~~text
Client
  |
  | POST /api/v1/notifications
  v
Spring Boot REST API
  |
  | publish + wait for broker acknowledgement
  v
Kafka: notifications.requested
  |
  v
Notification consumer
  |
  v
Demo delivery adapter
  |
  +-- failure --> 3 retries --> notifications.requested.DLT
~~~

## Stack

- Java 17, Spring Boot 3, Spring Web and Bean Validation
- Apache Kafka with JSON event serialization
- Spring Kafka error handler and dead-letter publishing recoverer
- Actuator health endpoint
- JUnit 5 and Mockito
- Docker Compose

## Run locally

Requirements: Docker with Compose and JDK 17+.

~~~bash
docker compose up --build
~~~

The API is available at http://localhost:8080. Health check: http://localhost:8080/actuator/health.

Run unit tests without starting Kafka:

~~~bash
mvn test
~~~

## Create a notification

~~~bash
curl -i -X POST http://localhost:8080/api/v1/notifications \
  -H 'Content-Type: application/json' \
  -d '{
    "requestId": "order-1001",
    "channel": "EMAIL",
    "recipient": "demo@example.com",
    "subject": "Payment received",
    "message": "Your payment was received."
  }'
~~~

The API returns HTTP 202 after Kafka acknowledges the event. Each event contains a generated notification ID and the supplied request ID as its Kafka message key. The service does not currently deduplicate repeated request IDs; this should not be confused with full idempotency.

## Retry and dead-letter behavior

A delivery exception is retried up to three times, waiting one second between attempts. When retries are exhausted, the record is published to the notifications.requested.DLT topic for investigation or replay.

The local demo configures three partitions and a single broker replica. A production cluster needs appropriate replication, monitoring and operational procedures for replaying dead-letter messages.

## Design decisions and production gaps

- The API confirms queueing only after broker acknowledgement; broker failures return HTTP 503.
- The delivery handler is a demo adapter. Replace it with provider clients behind a defined interface.
- The sample does not persist notification status or implement request-ID deduplication.
- A production version should add durable status tracking, idempotency storage, provider rate limits, delivery receipts, PII-safe logging, metrics, alerting, authentication and integration tests using a real Kafka broker.
- Never put credentials or real recipient data in source code or test fixtures.
