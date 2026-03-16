---
description: Create a full REST endpoint stack for a new resource
argument-hint: [resource-name]
allowed-tools: Read, Write, Bash(mvn *)
model: sonnet
---

Create a new REST endpoint for the resource: $ARGUMENTS

Follow these steps:
1. Create the JPA entity in the entity package
2. Create a Flyway migration for the table
3. Create the Spring Data JPA repository
4. Create request/response DTOs as Java records
5. Create the service class with CRUD methods
6. Create the RestController with standard CRUD endpoints
7. Create a @WebMvcTest controller test
8. Create a service unit test with Mockito
9. Run `mvn compile` to verify compilation
10. Run `mvn test` to verify all tests pass
