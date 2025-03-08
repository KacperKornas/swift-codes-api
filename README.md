# Swift Codes API

## Overview
Swift Codes API is a RESTful application designed to manage bank SWIFT codes. It allows you to retrieve, create, update, delete, and import SWIFT code data from an Excel file. The project uses Java, Spring Boot, JPA/Hibernate, and MySQL in production (with H2 used for testing). The application is containerized using Docker and Docker Compose.

## Features
- **Excel File Import** – Parses data from an Excel file, converting values to uppercase and distinguishing headquarters (codes ending with "XXX") from branches.
- **Retrieve SWIFT Code** – Returns detailed information for a given SWIFT code. For headquarters, the response includes a list of associated branches.
- **Retrieve Codes by Country** – Finds all SWIFT codes for a specific country using its ISO2 code.
- **CRUD Operations** – Create, update, and delete SWIFT code entries.
- **Containerization** – Dockerfile and docker-compose.yml files are provided to run the application and database in containers.

## Technologies
- Java 17
- Spring Boot 3.2.2
- Maven
- MySQL (production) / H2 (testing)
- Apache POI (for Excel parsing)
- Docker & Docker Compose

## Prerequisites
- Java 17
- Maven
- Docker and Docker Compose
- MySQL (if running outside of Docker)

## Environment Variables
In production, the application uses the following environment variables:
- `DB_USERNAME` – MySQL database username.
- `DB_PASSWORD` – MySQL database password.

These variables are used in the `src/main/resources/application.properties` file:

```properties
spring.application.name=swift-api

spring.datasource.url=jdbc:mysql://localhost:3306/swift_db
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
spring.jpa.hibernate.ddl-auto=update
```

## Running the Application

### Local Setup Using Maven
1. **Clone the repository:**
   ```bash
   git clone https://github.com/yourusername/swift-codes-api.git
   cd swift-codes-api
   ```
2. **Set environment variables:**
   ```bash
   export DB_USERNAME=your_mysql_username
   export DB_PASSWORD=your_mysql_password
   ```
3. **Build and run the application:**
   ```bash
   mvn clean package
   mvn spring-boot:run
   ```
   The application will be available at [http://localhost:8080](http://localhost:8080).

### Running with Docker Compose
1. Ensure Docker and Docker Compose are installed.
2. In the project’s root directory, run:
   ```bash
   docker-compose up --build
   ```
   This command builds the application image and starts containers for both MySQL and the API.
3. The API will be accessible at [http://localhost:8080](http://localhost:8080).

## Docker Configuration

### Dockerfile
The **Dockerfile** is located in the root of the project and defines how to build the application’s container image:
```dockerfile
FROM maven:3.8.5-openjdk-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=builder /app/target/swift-codes-api-1.0-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### docker-compose.yml
The **docker-compose.yml** file orchestrates the application along with a MySQL database container:
```yaml
version: "3.8"

services:
  mysql:
    image: mysql:8.0
    container_name: swift-mysql
    environment:
      MYSQL_DATABASE: swift_db
      MYSQL_ROOT_PASSWORD: rootpassword
      MYSQL_USER: swiftuser
      MYSQL_PASSWORD: swiftpass
    ports:
      - "3306:3306"
    volumes:
      - mysql_data:/var/lib/mysql
    command: --default-authentication-plugin=mysql_native_password

  swift-api:
    build: .
    container_name: swift-api
    environment:
      DB_USERNAME: swiftuser
      DB_PASSWORD: swiftpass
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/swift_db
    ports:
      - "8080:8080"
    depends_on:
      - mysql

volumes:
  mysql_data:
```

## Testing
To run the tests, execute:
```bash
mvn test
```
Tests are configured to use the in-memory H2 database (see `src/test/resources/application.properties`).

## API Endpoints

### GET `/v1/swift-codes/{swiftCode}`
Retrieves detailed information for the specified SWIFT code. If the code represents a headquarters, the response includes a list of branches.

**Example response:**
```json
{
  "swiftCode": "SWIFT123",
  "bankName": "Bank ABC",
  "address": "Headquarters address",
  "countryISO2": "PL",
  "countryName": "PL",
  "isHeadquarter": true,
  "branches": [
    {
      "swiftCode": "SWIFT1234",
      "bankName": "Bank ABC - Branch 1",
      "address": "Branch address",
      "countryISO2": "PL",
      "countryName": "PL",
      "isHeadquarter": false
    }
  ]
}
```

### GET `/v1/swift-codes/country/{countryISO2}`
Retrieves all SWIFT codes for the specified country (both headquarters and branches).

**Example response:**
```json
{
  "countryISO2": "PL",
  "countryName": "PL",
  "swiftCodes": [
    {
      "swiftCode": "SWIFT123",
      "bankName": "Bank ABC",
      "address": "Headquarters address",
      "countryISO2": "PL",
      "isHeadquarter": true
    },
    {
      "swiftCode": "SWIFT1234",
      "bankName": "Bank ABC - Branch 1",
      "address": "Branch address",
      "countryISO2": "PL",
      "isHeadquarter": false
    }
  ]
}
```

### POST `/v1/swift-codes`
Creates a new SWIFT code entry.

**Request body:**
```json
{
  "swiftCode": "SWIFT456",
  "bankName": "Bank XYZ",
  "address": "Address",
  "countryISO2": "DE",
  "countryName": "DE",
  "isHeadquarter": false
}
```

**Response:**
```json
{
  "message": "Swift code created successfully"
}
```

### PUT `/v1/swift-codes/{swiftCode}`
Updates an existing SWIFT code.

**Request body:**
```json
{
  "swiftCode": "SWIFT456",
  "bankName": "Bank XYZ Updated",
  "address": "New address",
  "countryISO2": "DE",
  "countryName": "DE",
  "isHeadquarter": false
}
```

**Response:**
```json
{
  "message": "Swift code updated successfully"
}
```

### DELETE `/v1/swift-codes/{swiftCode}`
Deletes the specified SWIFT code.

**Response:**
```json
{
  "message": "Swift code deleted successfully"
}
```

### POST `/v1/swift-codes/import`
Imports SWIFT codes from an Excel file. The file should be sent as multipart/form-data with the key `file`.

**Response:**
```json
{
  "message": "SWIFT codes imported successfully."
}
```

## Project Structure
```
swift-codes-api/
├── Dockerfile
├── docker-compose.yml
├── pom.xml
├── README.md
├── src/
│   ├── main/
│   │   ├── java/com/kacper/swiftapi/
│   │   │   ├── controller/
│   │   │   ├── dto/
│   │   │   ├── entity/
│   │   │   ├── repository/
│   │   │   ├── service/
│   │   │   └── utils/
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/com/kacper/swiftapi/
└── ...
```

## Summary
This project provides a complete implementation of a RESTful API for managing SWIFT codes, fully meeting the project requirements. The instructions cover environment configuration, building and running the application both locally and in containers, and details on all available endpoints.
