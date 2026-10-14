# Budget Tracker API

A Java and Spring Boot REST API for recording income and expense transactions. The application separates HTTP handling, request/response DTOs, business logic, and database access.

## Features

- Create, list, retrieve, update, and delete transactions.
- Store descriptions, amounts, transaction types, categories, and dates.
- Validate requests with Jakarta Bean Validation.
- Return JSON field errors for invalid requests and HTTP 404 for missing transaction IDs.
- Store amounts as `BigDecimal`, with a database precision of 19 digits and a scale of 2.
- Use Spring Data JPA with an H2 database.
- Provide a simple running-status endpoint and the H2 console for local development.

## Stack

The committed `pom.xml` targets **Java 25** and **Spring Boot 4.1.1**. It declares Spring MVC, Validation, Data JPA, H2, and Spring Boot testing dependencies. Maven is the build tool.

## Run locally

Prerequisites: JDK 25 and an installed Maven distribution compatible with the project dependencies.

```bash
git clone https://github.com/denus-marker/Budget-Tracker-API.git
cd Budget-Tracker-API/budget-tracker-api
mvn spring-boot:run
```

The application uses the default Spring Boot HTTP port, `8080`.

Although `mvnw` and `mvnw.cmd` are committed, the repository does not contain `.mvn/wrapper/maven-wrapper.properties`. Use installed Maven with the current checkout.

### Storage

The default configuration uses `jdbc:h2:mem:budgetdb`. Transactions exist only for the lifetime of the running application and are lost when it stops.

Open [the H2 console](http://localhost:8080/h2-console) while the application is running:

- JDBC URL: `jdbc:h2:mem:budgetdb`
- User: `sa`
- Password: empty

This configuration is intended for local development.

## REST endpoints

| Method | Path | Result |
| --- | --- | --- |
| GET | `/api/hello` | Plain text: `Budget tracker API is running` |
| GET | `/api/transactions` | All transactions as a JSON array |
| GET | `/api/transactions/{id}` | One transaction, or 404 |
| POST | `/api/transactions` | Create a transaction and return it |
| PUT | `/api/transactions/{id}` | Replace all editable fields, or 404 |
| DELETE | `/api/transactions/{id}` | Delete a transaction, or 404 |

Successful operations currently return HTTP 200; DELETE returns an empty body. POST does not explicitly return 201, and DELETE does not explicitly return 204.

### Request fields

POST and PUT use the same JSON body:

| Field | Type | Rules |
| --- | --- | --- |
| `description` | string | Required, non-blank, at most 255 characters |
| `amount` | number | Required, positive, at most 17 integer digits and 2 fractional digits |
| `type` | string | Required: `INCOME` or `EXPENSE` |
| `category` | string or null | Optional, at most 100 characters |
| `date` | string | Required, ISO date such as `2026-10-14` |

Responses contain these fields plus a generated `id`.

### Example

```bash
curl http://localhost:8080/api/hello

curl -X POST http://localhost:8080/api/transactions \
  -H 'Content-Type: application/json' \
  -d '{"description":"Groceries","amount":24.50,"type":"EXPENSE","category":"Food","date":"2026-10-14"}'

curl http://localhost:8080/api/transactions
```

Use an ID returned by POST or GET to retrieve, update, or delete a record:

```bash
curl http://localhost:8080/api/transactions/1

curl -X PUT http://localhost:8080/api/transactions/1 \
  -H 'Content-Type: application/json' \
  -d '{"description":"Weekly groceries","amount":30.00,"type":"EXPENSE","category":"Food","date":"2026-10-14"}'

curl -X DELETE http://localhost:8080/api/transactions/1
```

A missing transaction returns an error object such as:

```json
{"error":"Transaction with id: 1 not found."}
```

Bean Validation failures return HTTP 400 with a map from field names to validation messages. Malformed JSON and invalid enum values use Spring's default error handling.

## Structure

```text
budget-tracker-api/
  pom.xml
  src/main/java/com/denys/budgettracker/
    BudgetTrackerApiApplication.java
    controller/     HTTP endpoints
    dto/            Validated requests and response records
    exception/      Missing-record and validation error handling
    model/          JPA entity and INCOME/EXPENSE enum
    repository/     Spring Data JPA repository
    service/        CRUD logic and entity-to-DTO mapping
  src/main/resources/application.properties
  src/test/java/com/denys/budgettracker/
    BudgetTrackerApiApplicationTests.java
```

## Tests

From `budget-tracker-api/`:

```bash
mvn test
```

The committed test is a Spring Boot application-context loading test. Endpoint, validation, and service behavior do not yet have dedicated tests.

## Current scope

The project provides transaction CRUD. It does not currently implement authentication, per-user accounts, balance/statistics endpoints, category/date filters, pagination, CSV export, or a frontend. The default H2 database is not durable storage.
