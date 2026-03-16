# TaskManager — Spring Boot + React Application

## Project Overview
A task management application with a Spring Boot 3.3 backend (REST API),
PostgreSQL database, and React + MUI frontend.

## Tech Stack
- Java 21, Spring Boot 3.3, Spring Data JPA, Spring Security
- PostgreSQL 16 with Flyway migrations
- React 18, TypeScript, Material UI v5
- Maven (multi-module: backend, frontend)
- Docker, Docker Compose for local development
- JUnit 5, Testcontainers, MockMvc for testing

## Project Structure

task-manager/
├── backend/                   # Spring Boot module
│   ├── src/main/java/com/example/taskmanager/
│   │   ├── config/            # Spring configuration classes
│   │   ├── controller/        # REST controllers
│   │   ├── dto/               # Request/Response DTOs
│   │   ├── entity/            # JPA entities
│   │   ├── exception/         # Exception handling
│   │   ├── mapper/            # MapStruct or manual mappers
│   │   ├── repository/        # Spring Data repositories
│   │   └── service/           # Business logic
│   ├── src/main/resources/
│   │   ├── application.yml
│   │   └── db/migration/      # Flyway SQL migrations
│   └── src/test/java/         # Tests mirror main structure
├── frontend/                  # React app
│   ├── src/
│   │   ├── api/               # Axios API clients
│   │   ├── components/        # Reusable MUI components
│   │   ├── pages/             # Route-level page components
│   │   ├── hooks/             # Custom React hooks
│   │   └── types/             # TypeScript interfaces
│   └── package.json
├── docker-compose.yml
├── CLAUDE.md                  # This file
└── pom.xml                    # Parent POM


## Coding Conventions

### Java/Spring
- Use records for DTOs: `public record TaskRequest(String title, String description) {}`
- Use constructor injection (no @Autowired on fields)
- Every public service method has a corresponding unit test
- Repository layer: use Spring Data JPA query methods, @Query for complex cases
- Naming: `*Controller`, `*Service`, `*Repository`, `*Dto`, `*Entity`
- Validation: use Jakarta Bean Validation annotations on DTOs
- Exceptions: use @RestControllerAdvice with ProblemDetail (RFC 7807)

### Database
- All schema changes via Flyway: `V{number}__{description}.sql`
- Table names: snake_case, plural (e.g., `tasks`, `user_profiles`)
- Always add indexes for foreign keys and frequently queried columns
- Use `BIGSERIAL` for primary keys

### Testing
- Unit tests: JUnit 5 + Mockito, suffix `*Test`
- Integration tests: @SpringBootTest + Testcontainers, suffix `*IT`
- Controller tests: @WebMvcTest + MockMvc
- Minimum: test happy path + one failure case per endpoint
- Use AssertJ for assertions

### React/Frontend
- Functional components only, TypeScript strict mode
- MUI components for all UI elements — no custom CSS unless necessary
- API calls via Axios with a centralized apiClient instance
- Error handling: MUI Snackbar for user feedback

## Commands
- Build backend: `cd backend && mvn clean package -DskipTests`
- Run tests: `cd backend && mvn test`
- Run integration tests: `cd backend && mvn verify -Pintegration`
- Start everything: `docker compose up --build`
- Frontend dev: `cd frontend && npm run dev`

## Important Rules
- NEVER commit code that doesn't compile
- NEVER skip writing tests — every feature PR must include tests
- ALWAYS use DTOs in controllers, never expose entities directly
- ALWAYS handle exceptions gracefully with proper HTTP status codes
- Database credentials come from environment variables, never hardcoded

