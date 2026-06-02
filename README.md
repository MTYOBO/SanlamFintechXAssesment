# Bank Withdrawal Service

A Spring Boot service for handling bank account withdrawals with an in-memory H2 database and AWS SNS event publication.

## Overview

This project exposes a single REST endpoint for withdrawing funds from a bank account. It uses:

- Java 21
- Spring Boot 3.3.5
- Spring Web
- Spring JDBC with H2 in-memory database
- AWS SDK for SNS
- LocalStack-compatible SNS configuration for local development

## Features

- `POST /bank/withdraw` endpoint
- Account balance lookup and update via JDBC
- Insufficient funds handling
- SNS event publishing for successful withdrawals
- H2 console enabled for local inspection
- Data initialization from `schema.sql` and `data.sql`

## Prerequisites

- Java 21
- Maven (or use the included Maven wrapper `./mvnw`)
- Docker if you want to run LocalStack locally

## Run Locally

1. Start LocalStack (optional but recommended for SNS publishing):

```bash
docker run --rm -p 4566:4566 localstack/localstack
```

2. Run the application:

```bash
cd bankwithdrawal
./mvnw spring-boot:run
```

The application starts on `http://localhost:8080`.

## API Usage

Withdraw funds from an account:

```bash
curl -X POST "http://localhost:8080/bank/withdraw?accountId=1&amount=100.00"
```

Response values:

- `Withdrawal successful`
- `Withdrawal failed`
- `Insufficient funds for withdrawal`
- `An error occurred...`

## Database and Sample Data

The application uses an in-memory H2 database configured in `src/main/resources/application.properties`:

- JDBC URL: `jdbc:h2:mem:dcbapp`
- H2 console: `http://localhost:8080/h2-console`

Initial sample accounts are loaded from `src/main/resources/data.sql`:

- Account `1` with balance `1000.00`
- Account `2` with balance `2500.50`

Schema is defined in `src/main/resources/schema.sql`.

## Configuration

Default application properties are in `src/main/resources/application.properties`.

Key values:

- `spring.datasource.url=jdbc:h2:mem:dcbapp`
- `spring.h2.console.enabled=true`
- `aws.region=us-east-1`
- `aws.sns.endpoint=http://localhost:4566`
- `aws.accessKeyId=local`
- `aws.secretAccessKey=local`

The SNS topic ARN is configured with a default value in `AwsConfig`:

- `arn:aws:sns:us-east-1:000000000000:withdrawal-events`

Update `aws.sns.topic-arn` or the `AwsConfig` bean as needed for your environment.

## Build

Package the application with Maven:

```bash
./mvnw clean package
```

Run the packaged JAR:

```bash
java -jar target/bankwithdrawal-0.0.1-SNAPSHOT.jar
```

## Tests

Run the test suite:

```bash
./mvnw test
```

## Notes

- The SNS publisher is configured to use LocalStack on `http://localhost:4566`.
- The repository uses `JdbcTemplate` for account balance operations.
- The event payload is published as a JSON string with `accountId`, `amount`, and `status`.

## Project Structure

- `src/main/java/com/sanlamfintechx/bankwithdrawal/controller` — REST controller
- `src/main/java/com/sanlamfintechx/bankwithdrawal/service` — business logic
- `src/main/java/com/sanlamfintechx/bankwithdrawal/repository` — database access
- `src/main/java/com/sanlamfintechx/bankwithdrawal/publisher` — SNS event publishing
- `src/main/resources` — application configuration and SQL initialization
