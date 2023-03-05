# Space Port

Jenkins Pipeline Assignment 2023

## Todo

- [ ] description
- [ ] requirements

## Table of Contents

* [Features](#features)
  * [Hangar microservice](#hangar-microservice)
    * [API](#api)
    * [Spaceship JSON schema](#spaceship-json-schema)
    * [Error JSON schema](#error-json-schema)
* [Setup](#setup)
  * [Run the application locally](#run-the-application-locally)
  * [Tests](#tests)
  * [Requirements](#requirements)

## Features

- Postgres database
- CRUD operations
- Request data validation
- Global exception handling
- Custom mapper for DTOs
- Unit tests with Junit5 and REST-assured
- Integration tests with Testcontainers
- Swagger documentation

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

Available endpoints:

```
GET     /api/v1/spaceships
GET     /api/v1/spaceships/{id}
POST    /api/v1/spaceships
PUT     /api/v1/spaceships/{id}
DELETE  /api/v1/spaceships/{id}
```

#### Spaceship JSON schema

```json
 {
  "id": "9e075e25-3bd8-466e-b978-cf38a07ff85b",
  "name": "string",
  "maxSpeed": 0,
  "payload": 0,
  "crewIds": [
    "8ed6e335-56cb-4512-b1fb-5a55faa1057c",
    "e1d4e41b-c72e-4fb7-b3bd-6b86e96b20f1"
  ],
  "armament": [
    "string"
  ],
  "class": "string"
}
```

#### Error JSON schema

```json
{
  "status": 0,
  "error": "string",
  "message": "string",
  "errors": [
    {
      "field": "string",
      "message": "string"
    }
  ]
}
```

## Setup

### Run the application locally

Start 2 Postgres containers, one for each service (it will create required table and seed dummy data):

```bash
docker compose up -d
```

Start the application in your IDE.

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
