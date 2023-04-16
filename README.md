# Space Port

Jenkins Pipeline Assignment 2023

## Table of Contents

* [Features](#features)
  * [Hangar microservice](#hangar-microservice)
    * [API](#api)
* [Setup](#setup)
  * [Run the application locally](#run-the-application-locally)
  * [Tests](#tests)
  * [Requirements](#requirements)

### Hangar microservice

#### API

Spaceship APIs exposed at:

```
http://localhost:8082/api/v1/spaceships
```

Swagger UI available at:

```
http://localhost:8082/
```

## Setup

### Run the application locally

Start Postgres container (it will create required table and seed dummy data):

```bash
docker compose up -d
```

Start the application in your IDE.

### Run the application locally using Docker Compose

```bash
docker compose --profile app up
```

### Tests

Run unit tests:

```bash
mvn test
```

Run unit and integration tests:

```bash
mvn clean verify
```

### Requirements

- Java 17
- Docker
- Maven
