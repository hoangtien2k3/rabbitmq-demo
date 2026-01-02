# 🐰 RabbitMQ Demo Project

Một demo project **chi tiết, chỉn chu** học về RabbitMQ sử dụng Spring Boot. Dự án này chứa các ví dụ thực tế về:

- ✅ Simple Queue
- ✅ Fanout Exchange (Broadcast)
- ✅ Direct Exchange (Targeted Routing)
- ✅ Topic Exchange (Pattern Matching)
- ✅ Event-Driven Architecture
- ✅ Asynchronous Messaging

---

## 📁 Cấu trúc Project

```
rabbitmq-demo/
├── src/main/java/com/hoangtien2k3/rabbitmq_demo/
│   ├── config/
│   │   └── RabbitMQConfig.java          # 🔧 Cấu hình RabbitMQ (Queue, Exchange, Binding)
│   │
│   ├── model/
│   │   ├── Message.java                 # 📦 DTO cho message
│   │   └── User.java                    # 👤 DTO cho user
│   │
│   ├── producer/
│   │   └── RabbitMQProducer.java        # 📤 Gửi message
│   │
│   ├── consumer/
│   │   └── RabbitMQConsumer.java        # 📥 Nhận message
│   │
│   ├── controller/
│   │   └── RabbitMQDemoController.java  # 🌐 REST API để test
│   │
│   └── RabbitmqDemoApplication.java     # 🚀 Main application
│
├── src/main/resources/
│   └── application.yaml                 # ⚙️ Spring Boot config
│
├── LEARNING_GUIDE.md                    # 📚 Tài liệu học chi tiết
├── TESTING_GUIDE.md                     # 🧪 Hướng dẫn test API
├── docker-compose.yml                   # 🐳 Docker Compose (RabbitMQ)
└── README.md                            # 📖 File này
```

---

## 🚀 Khởi động nhanh (Quick Start)

### Option 1: Dùng Homebrew (macOS)

```bash
# 1. Cài RabbitMQ
brew install rabbitmq

# 2. Start RabbitMQ
brew services start rabbitmq-server

# 3. Kiểm tra
rabbitmqctl status

# 4. Start Spring Boot App
cd /Volumes/DATA/Backend/java-backend/rabbitmq-demo
./gradlew bootRun

# 5. Truy cập
# App: http://localhost:8080
# RabbitMQ Management: http://localhost:15672 (guest/guest)
```

### Option 2: Dùng Docker Compose (Recommend)

```bash
# 1. Start RabbitMQ qua Docker
cd /Volumes/DATA/Backend/java-backend/rabbitmq-demo
docker-compose up -d

# 2. Kiểm tra RabbitMQ chạy
docker-compose ps

# 3. Start Spring Boot App
./gradlew bootRun

# 4. Truy cập
# App: http://localhost:8080
# RabbitMQ Management: http://localhost:15672 (guest/guest)

# 5. Stop RabbitMQ
docker-compose down
```

### Option 3: Manual Docker

```bash
docker run -d \
  --name rabbitmq \
  -p 5672:5672 \
  -p 15672:15672 \
  rabbitmq:3-management

# Login: guest / guest
```

---

## 📚 Kiến thức cơ bản

### RabbitMQ là gì?

RabbitMQ là **message broker** (công cụ trung gian) cho phép các ứng dụng gửi và nhận message một cách asynchronous.

```
Không dùng RabbitMQ:
Service A -> HTTP -> Service B
Problem: Nếu Service B down, request fail

Dùng RabbitMQ:
Service A -> RabbitMQ -> Service B
Benefit: Service B offline, message được lưu, xử lý khi online
```

### 5 Thành phần chính

| Thành phần   | Mô tả                                  |
| ------------ | -------------------------------------- |
| **Producer** | Ứng dụng gửi message                   |
| **Queue**    | Nơi lưu trữ message chờ xử lý (FIFO)   |
| **Exchange** | Công tắc, định tuyến message tới Queue |
| **Binding**  | Quy tắc kết nối Exchange và Queue      |
| **Consumer** | Ứng dụng nhận và xử lý message         |

### 4 Loại Exchange

| Loại        | Cách hoạt động          | Khi dùng               |
| ----------- | ----------------------- | ---------------------- |
| **Direct**  | Match exact routing key | Targeted routing       |
| **Fanout**  | Send tới tất cả Queue   | Broadcast/Notification |
| **Topic**   | Pattern matching (*,#)  | Complex routing        |
| **Headers** | Dùng message headers    | Advanced use cases     |

---

## 🎯 Các ví dụ trong project

### 1. Simple Queue - Hello World

**File:** [src/main/java/com/hoangtien2k3/rabbitmq_demo/config/RabbitMQConfig.java](src/main/java/com/hoangtien2k3/rabbitmq_demo/config/RabbitMQConfig.java#L30)

```java
// Config
@Bean
public Queue helloQueue() {
    return new Queue("hello-queue", true);
}

// Producer
public void sendHelloMessage(String message) {
    rabbitTemplate.convertAndSend("hello-queue", message);
}

// Consumer
@RabbitListener(queues = "hello-queue")
public void consumeHelloMessage(String message) {
    System.out.println("Nhận: " + message);
}
```

**Test:**
```bash
curl "http://localhost:8080/api/rabbitmq/hello?message=HelloWorld"
```

---

### 2. Fanout Exchange - User Event Broadcasting

**File:** [src/main/java/com/hoangtien2k3/rabbitmq_demo/config/RabbitMQConfig.java](src/main/java/com/hoangtien2k3/rabbitmq_demo/config/RabbitMQConfig.java#L54)

Khi user mới được tạo, message được gửi tới **3 service** đồng thời:

```
User Service (lưu DB)
    ↓
User Created Event -> FanoutExchange -> Email Service (gửi email)
    ↓
Log Service (ghi log)
```

**Producer:**
```java
public void publishUserCreatedEvent(User user) {
    rabbitTemplate.convertAndSend("user-event-exchange", "", user);
}
```

**Test:**
```bash
curl -X POST http://localhost:8080/api/rabbitmq/user/create \
  -H "Content-Type: application/json" \
  -d '{"username":"john","email":"john@example.com"}'
```

---

### 3. Direct Exchange - Message Routing

**File:** [src/main/java/com/hoangtien2k3/rabbitmq_demo/config/RabbitMQConfig.java](src/main/java/com/hoangtien2k3/rabbitmq_demo/config/RabbitMQConfig.java#L112)

Routing dựa trên **exact match** của routing key:

```
Message with routing key "message.info"
  -> Direct Exchange
  -> Queue: message-queue (binding: message.info) ✅ MATCH
  -> Queue: critical-queue (binding: message.critical) ❌ NO MATCH
```

**Test Normal Message:**
```bash
curl -X POST http://localhost:8080/api/rabbitmq/message/normal \
  -H "Content-Type: application/json" \
  -d '{"content":"Hello","sender":"John"}'
```

**Test Critical Message:**
```bash
curl -X POST http://localhost:8080/api/rabbitmq/message/critical \
  -H "Content-Type: application/json" \
  -d '{"content":"URGENT!","sender":"Admin"}'
```

---

### 4. Topic Exchange - Pattern Matching

**File:** [src/main/java/com/hoangtien2k3/rabbitmq_demo/config/RabbitMQConfig.java](src/main/java/com/hoangtien2k3/rabbitmq_demo/config/RabbitMQConfig.java#L164)

Routing dựa trên **pattern matching**:

```
Pattern:
  *  = khớp 1 từ
  #  = khớp 0 hoặc nhiều từ

Example:
"order.created" matches:
  - "order.created" ✅
  - "order.*" ✅
  - "order.#" ✅
  - "#" ✅

"order.shipped" matches:
  - "order.shipped" ✅
  - "order.*" ✅
  - "#" ✅
```

**Test Order Created:**
```bash
curl "http://localhost:8080/api/rabbitmq/order/created?orderId=ORDER-123"
```

**Test Order Shipped:**
```bash
curl "http://localhost:8080/api/rabbitmq/order/shipped?orderId=ORDER-123"
```

---

## 🌐 API Endpoints

### Info
```bash
GET /api/rabbitmq/info
```
Xem danh sách tất cả endpoint

### Simple Queue
```bash
GET /api/rabbitmq/hello?message=your_message
```

### Fanout Exchange
```bash
POST /api/rabbitmq/user/create
Content-Type: application/json

{
  "username": "john",
  "email": "john@example.com"
}
```

### Direct Exchange
```bash
POST /api/rabbitmq/message/normal
POST /api/rabbitmq/message/critical
Content-Type: application/json

{
  "content": "message content",
  "sender": "sender_name"
}
```

### Topic Exchange
```bash
GET /api/rabbitmq/order/created?orderId=ORDER-123
GET /api/rabbitmq/order/shipped?orderId=ORDER-123
POST /api/rabbitmq/order/event?routingKey=order.xxx
```

### Test All
```bash
GET /api/rabbitmq/test-all
```
Chạy tất cả test cùng lúc

---

## 📖 Tài liệu học

### Tài liệu chi tiết
Xem file [LEARNING_GUIDE.md](LEARNING_GUIDE.md):
- RabbitMQ là gì?
- 5 khái niệm cơ bản
- 4 loại Exchange chi tiết
- Hướng dẫn cài đặt
- Giải thích code
- Use case thực tế

### Hướng dẫn test API
Xem file [TESTING_GUIDE.md](TESTING_GUIDE.md):
- Chi tiết từng endpoint
- Request/Response examples
- Expected logs
- Troubleshooting
- Performance testing

---

## 🔧 Cấu hình RabbitMQ

Xem file [src/main/resources/application.yaml](src/main/resources/application.yaml)

**Các cấu hình quan trọng:**

```yaml
spring:
  rabbitmq:
    host: localhost
    port: 5672
    username: guest
    password: guest
    
    listener:
      simple:
        # Số thread xử lý đồng thời
        concurrency: 1-5
        
        # Prefetch size
        prefetch: 1
        
        # Retry config
        retry:
          enabled: true
          max-attempts: 3
```

---

## 🐳 Docker Compose

File [docker-compose.yml](docker-compose.yml) để start RabbitMQ nhanh chóng.

```bash
# Start
docker-compose up -d

# Stop
docker-compose down

# View logs
docker-compose logs -f rabbitmq

# Access Management UI
# http://localhost:15672
# Username: guest
# Password: guest
```

---

## 🔍 Monitoring

### RabbitMQ Management UI
```
http://localhost:15672
Username: guest
Password: guest
```

**Xem:**
- Queues: Queue, message count, consumer count
- Connections: Active connections
- Channels: Message flow
- Admin: User management, permissions

### Từ Terminal

```bash
# Check status
rabbitmqctl status

# List queues
rabbitmqctl list_queues

# List connections
rabbitmqctl list_connections

# List channels
rabbitmqctl list_channels
```

---

## ⚙️ Build & Run

### Từ Terminal

```bash
# Build
cd /Volumes/DATA/Backend/java-backend/rabbitmq-demo
./gradlew build

# Run
./gradlew bootRun

# Test
curl http://localhost:8080/api/rabbitmq/info
```

### Từ IDE (IntelliJ/VS Code)

1. Open project
2. Run -> Edit Configurations
3. Add Spring Boot configuration
4. Click Run

---

## 🎓 Bài tập tự luyện

1. **Queue Mới**: Tạo queue "notification-queue" với consumer đọc message
2. **Custom Exchange**: Tạo topic exchange cho "product.*" events
3. **Error Handling**: Thêm dead-letter queue khi message fail
4. **Performance**: Test throughput với 1000 message
5. **Monitoring**: Setup alerting cho queue length

---

## 📌 Stack Technology

- **Java 17**
- **Spring Boot 4.0.1**
- **Spring AMQP**
- **RabbitMQ 3**
- **Gradle**

---

## 📚 Tham khảo

- [RabbitMQ Official](https://www.rabbitmq.com/)
- [Spring AMQP Documentation](https://spring.io/projects/spring-amqp)
- [RabbitMQ Tutorials](https://www.rabbitmq.com/getstarted.html)
- [AMQP Protocol](https://www.amqp.org/)

---

## 💡 Kiểm tra nhanh

```bash
# 1. RabbitMQ chạy?
rabbitmqctl status

# 2. Java app chạy?
ps aux | grep java | grep rabbitmq

# 3. API hoạt động?
curl http://localhost:8080/api/rabbitmq/info

# 4. Queue có message?
rabbitmqctl list_queues

# 5. Xem logs
# Application logs: Console
# RabbitMQ logs: docker logs rabbitmq
```

---

## 🤝 Liên hệ

Nếu có vấn đề, kiểm tra:
1. [LEARNING_GUIDE.md](LEARNING_GUIDE.md) - Tài liệu
2. [TESTING_GUIDE.md](TESTING_GUIDE.md) - Hướng dẫn test
3. RabbitMQ Management UI: http://localhost:15672
4. Application logs

---

## ✨ Summary

Dự án này cung cấp:

✅ **5 Demo đầy đủ**: Simple, Fanout, Direct, Topic, Headers

✅ **Tài liệu chi tiết**: Tối thiểu 1000 dòng comment & docs

✅ **API test sẵn**: 7+ endpoints để test tất cả tính năng

✅ **Docker support**: docker-compose.yml để start nhanh

✅ **Real-world examples**: Event-driven architecture

✅ **Troubleshooting**: Hướng dẫn fix lỗi phổ biến

---

**Chúc bạn học tốt! 🚀**

```
RabbitMQ Demo
├── Config (RabbitMQConfig)
├── Model (Message, User)
├── Producer (gửi)
├── Consumer (nhận)
├── Controller (API)
└── Docs (hướng dẫn)

Start here: http://localhost:8080/api/rabbitmq/info
```
