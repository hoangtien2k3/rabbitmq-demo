# 🐰RabbitMQ Demo with Spring Boot

A comprehensive demonstration of RabbitMQ messaging patterns implemented using Spring Boot 4.0.1 and Java 17.

## 🚀 Key Features

- **Simple Queue**: Basic message sending and receiving.
- **Direct Exchange**: Targeted routing based on exact routing keys.
- **Fanout Exchange**: Broadcasting messages to multiple queues.
- **Topic Exchange**: Pattern-based routing using wildcard characters.
- **Headers Exchange**: Routing based on message header attributes.

## 🛠 Tech Stack

- **Java 17**
- **Spring Boot 4.0.1**
- **Spring AMQP (RabbitMQ)**
- **Gradle**
- **Docker & Docker Compose**

## 🏁 Quick Start

1. **Start RabbitMQ**:
   ```bash
   docker-compose up -d
   ```
2. **Run Application**:
   ```bash
   ./gradlew bootRun
   ```

## 🌐 API Endpoints

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/api/rabbitmq/info` | List all available test endpoints |
| `GET` | `/api/rabbitmq/hello` | Send a "Hello World" message |
| `POST` | `/api/rabbitmq/user/create` | Test Fanout (User creation event) |
| `POST` | `/api/rabbitmq/message/normal` | Test Direct (Normal message) |
| `POST` | `/api/rabbitmq/message/critical` | Test Direct (Critical/Error message) |
| `GET` | `/api/rabbitmq/order/created` | Test Topic (Order created event) |
| `GET` | `/api/rabbitmq/order/shipped` | Test Topic (Order shipped event) |
| `GET` | `/api/rabbitmq/test-all` | Execute all test scenarios at once |

## 📊 Monitoring

- **RabbitMQ Management UI**: [http://localhost:15672](http://localhost:15672) (guest/guest)
- **Application URL**: [http://localhost:8080](http://localhost:8080)
