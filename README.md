# Hospital Management System

## Project Structure
```
hospital-management/
│
├── postman/
│   └── Hospital.postman_collection.json
│
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── hospital/
│       │           ├── config/
│       │           ├── controller/
│       │           ├── dto/
│       │           ├── exception/
│       │           ├── model/
│       │           ├── repository/
│       │           ├── response/
│       │           ├── security/
│       │           └── service/
│       │           └── HospitalApplication.java
│       └── resources/
│           ├── static/
│           ├── templates/
│           └── application.yml
│
└── pom.xml
```

## Description
Hospital Management System built using Spring Boot for backend. This system manages hospital operations such as patient management, appointments, and staff management.

## Modules Overview
- **config/**: Configuration files for security, CORS, and other settings.
- **controller/**: Handles incoming API requests and maps them to the appropriate services.
- **dto/**: Data Transfer Objects for handling request and response data.
- **exception/**: Custom exception handling classes.
- **model/**: Entity classes representing the database structure.
- **repository/**: Interfaces for database operations using Spring Data JPA.
- **response/**: Standard response structures for API responses.
- **security/**: Security configurations and JWT authentication.
- **service/**: Business logic and service layer.
- **resources/**: Contains static resources, templates, and configuration files like `application.yml`.

## Postman Collection
The `Hospital.postman_collection.json` file contains pre-configured API requests for testing endpoints.

### How to Use Postman Collection
1. Open Postman.
2. Click on `File > Import`.
3. Select the `Hospital.postman_collection.json` file from the `postman/` directory.
4. After importing, you will see the collection in the Postman sidebar.
5. Select the desired API endpoint and configure any necessary parameters or headers.
6. Click `Send` to make the request and view the response.

## Prerequisites
- Java 17 or later
- Spring Boot
- com.h2database database
- Maven

## Dependencies (From `pom.xml`)
- Spring Boot Starter (Web, Security, Data JPA, Validation)
- Springfox Swagger for API documentation
- ModelMapper for object mapping
- Lombok for reducing boilerplate code
- JWT (JSON Web Token) for authentication
- H2 Database (for testing purposes)
- Hibernate Validator for input validation
- JUnit and Mockito for testing
- Spring Cloud dependencies for microservices architecture

## How to Run
1. Clone the repository.
2. Navigate to the project directory.
3. Build the project using Maven:
   ```bash
   mvn clean install
   ```
4. Run the application:
   ```bash
   mvn spring-boot:run
   ```
5. Access the API via `http://localhost:8080`.

## API Documentation
Use the Postman collection or Swagger UI for testing the endpoints.

