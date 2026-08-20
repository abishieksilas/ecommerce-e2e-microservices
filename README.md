# E-Commerce Microservices
# Simple Spring Boot 3 + Spring Cloud learning project (Java 21, Maven, MySQL)

## What you built

| Module | Port | Role |
|--------|------|------|
| service-registry | 8761 | Eureka Server (service discovery) |
| config-server | 8888 | Central configuration |
| api-gateway | 8080 | Single entry point for clients |
| admin-server | 8090 | Spring Boot Admin UI |
| product-service | 8081 | Product CRUD (`product_db`) |
| inventory-service | 8082 | Stock check/update (`inventory_db`) |
| order-service | 8083 | Place/view orders (`order_db`) + Feign → inventory |
| common-library | — | Shared DTOs / exceptions (jar) |

---

## Prerequisites

1. **JDK 21**
2. **Maven 3.9+** (or IntelliJ bundled Maven)
3. **MySQL 8.x** running locally
4. **Zipkin** (optional, for tracing) — download the Java jar from [Zipkin Quickstart](https://zipkin.io/pages/quickstart.html) and run:
   ```bash
   java -jar zipkin-server-*-exec.jar
   ```
   UI: http://localhost:9411

Update MySQL username/password in:
- each service `application.yml`
- files under `config-repo/`

Default used in this project: `root` / `root`

---

## Folder structure

```
ecommers/
├── pom.xml                          # Parent aggregator POM
├── config-repo/                     # Central configs (native or Git)
├── sql/init-databases.sql
├── common-library/
├── service-registry/
├── config-server/
├── api-gateway/
├── admin-server/
├── product-service/
├── inventory-service/
└── order-service/
```

Each business service uses packages: `controller`, `service`, `repository`, `entity`, `dto` (via common-library), `config`, `exception`.

---

## Module explanations

### 1. Parent `pom.xml`
Centralizes Spring Boot `3.3.5`, Spring Cloud `2023.0.3`, Java 21, Lombok, and module list. Children inherit versions via `dependencyManagement`.

### 2. `common-library`
Shared `ApiResponse`, request/response DTOs, `ResourceNotFoundException`, `BusinessException`. Not a runnable app.

### 3. `service-registry` (Eureka)
- `@EnableEurekaServer`
- Does **not** register itself (`register-with-eureka: false`)
- Dashboard: http://localhost:8761

**Why Eureka?** Services should not hardcode host/port. Eureka is a live phone book.

**Registration:** On startup, each Eureka client sends its name + host + port and renews with heartbeats.

**Discovery:** Gateway/Feign ask Eureka for `inventory-service` and get a healthy instance URL.

### 4. `config-server`
- `@EnableConfigServer`
- Default profile: **native** → reads `../config-repo/*.yml` (works without Git)
- Optional profile **git**: use `--spring.profiles.active=git` after `git init` inside `config-repo`

Test: http://localhost:8888/product-service/default

### 5. `api-gateway`
Routes (from `config-repo/api-gateway.yml`):

| Client path | Forwards to |
|-------------|-------------|
| `/api/products/**` | `lb://product-service` → `/products/**` |
| `/api/inventory/**` | `lb://inventory-service` → `/inventory/**` |
| `/api/orders/**` | `lb://order-service` → `/orders/**` |

`lb://` = Eureka + LoadBalancer lookup (no hardcoded ports).

### 6. `admin-server`
- `@EnableAdminServer` + Eureka discovery
- UI: http://localhost:8090
- Monitor health, JVM memory, CPU, beans, metrics via Actuator on each client

### 7. `product-service`
CRUD on `products` table. Swagger: http://localhost:8081/swagger-ui.html

### 8. `inventory-service`
`PUT /inventory`, `GET /inventory/check`, `POST /inventory/decrease`, `GET /inventory/{productId}`

### 9. `order-service` + Feign
`InventoryClient` (`@FeignClient(name = "inventory-service")`) calls inventory before saving an order.

**Feign internally:** interface → dynamic proxy → build HTTP from annotations → resolve service via Eureka → decode JSON response.

---

## Actuator endpoints

| Endpoint | Meaning |
|----------|---------|
| `/actuator/health` | UP/DOWN + component status (DB, disk, etc.) |
| `/actuator/info` | App metadata from `info.*` properties |
| `/actuator/metrics` | List of Micrometer metrics; drill into `/actuator/metrics/{name}` |

---

## Zipkin tracing

Request: Client → Gateway → Order Service → Inventory Service

- **Trace ID**: one ID for the whole request across services
- **Span ID**: one unit of work inside a service (gateway hop, DB call, Feign call)

Open Zipkin UI → find the trace for `POST /api/orders`.

---

## Database setup

```sql
-- Run: sql/init-databases.sql
```

Creates `product_db`, `inventory_db`, `order_db`. Hibernate `ddl-auto: update` also creates tables from entities.

---

## Run order in IntelliJ IDEA

1. **File → Open** → select the root `ecommers` folder (parent POM).
2. Wait for Maven import. Set **Project SDK = 21**.
3. Run MySQL; execute `sql/init-databases.sql`.
4. (Optional) Start Zipkin on port 9411.
5. Start applications **in this order** (right-click each `*Application` → Run):

   1. `ServiceRegistryApplication`
   2. `ConfigServerApplication`
   3. `AdminServerApplication`
   4. `ApiGatewayApplication`
   5. `ProductServiceApplication`
   6. `InventoryServiceApplication`
   7. `OrderServiceApplication`

6. Verify Eureka UI shows all services: http://localhost:8761

**Tip:** Edit Run Configuration → Working directory = `$MODULE_DIR$` for each module (config-server expects `../config-repo`).

---

## Postman test flow

Base URL through gateway: `http://localhost:8080`

### 1) Create product
`POST http://localhost:8080/api/products`  
Body:
```json
{
  "name": "Wireless Mouse",
  "description": "Ergonomic wireless mouse",
  "price": 799.00
}
```

### 2) Set stock (use returned product id)
`PUT http://localhost:8080/api/inventory`  
Body:
```json
{
  "productId": 1,
  "quantity": 50
}
```

### 3) Check stock
`GET http://localhost:8080/api/inventory/check?productId=1&quantity=2`

### 4) Place order
`POST http://localhost:8080/api/orders`  
Body:
```json
{
  "productId": 1,
  "quantity": 2,
  "unitPrice": 799.00
}
```

### 5) View order
`GET http://localhost:8080/api/orders/1`

### 6) List products
`GET http://localhost:8080/api/products`

Expected place-order path:
```
Client → Gateway → Order Service → Feign → Inventory (check)
                 → Feign → Inventory (decrease)
                 → Save order in order_db → 201 Created
```

---

## Useful URLs

| Tool | URL |
|------|-----|
| Eureka | http://localhost:8761 |
| Config example | http://localhost:8888/product-service/default |
| Gateway | http://localhost:8080 |
| Admin UI | http://localhost:8090 |
| Zipkin | http://localhost:9411 |
| Product Swagger | http://localhost:8081/swagger-ui.html |
| Inventory Swagger | http://localhost:8082/swagger-ui.html |
| Order Swagger | http://localhost:8083/swagger-ui.html |

---

## Config property cheat-sheet (`config-repo`)

| Property | Meaning |
|----------|---------|
| `server.port` | HTTP port of that service |
| `spring.datasource.*` | MySQL connection for that service’s DB |
| `spring.jpa.hibernate.ddl-auto=update` | Auto-create/update tables (learning only) |
| `eureka.client.service-url.defaultZone` | Eureka server URL |
| `management.endpoints.web.exposure.include` | Which Actuator endpoints are public |
| `management.tracing.sampling.probability` | Fraction of traces sent to Zipkin (`1.0` = all) |
| `management.zipkin.tracing.endpoint` | Where spans are POSTed |
| `spring.cloud.gateway.routes` | Gateway predicates (`Path`) + filters (`RewritePath`) + `uri: lb://...` |

---

## Notes for learners

- No Security / OAuth2 / Kafka / Redis / Docker / tests — intentionally omitted.
- Change DB password in both local `application.yml` and `config-repo` files.
- `unitPrice` on place-order keeps the demo simple (no Feign call to product-service).
- Prefer constructor injection; Lombok reduces boilerplate; always return `ResponseEntity` + `ApiResponse`.
