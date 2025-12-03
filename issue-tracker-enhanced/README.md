# Issue Tracker - Enhanced (Intern submission)

Features included:
1. JWT Authentication + RBAC (ADMIN, DEVELOPER, REPORTER)
2. Comments on issues (CRUD)
3. Activity log (audit trail) recorded automatically on issue create/update/delete
4. Pagination, filtering and sorting for issue listing
5. Swagger/OpenAPI UI

## Quick run
Requirements: Java 17+, Maven

1. Build & run:
   mvn spring-boot:run

2. Open API docs (Swagger UI):
   http://localhost:8080/swagger-ui/index.html

3. H2 console:
   http://localhost:8080/h2-console
   JDBC URL: jdbc:h2:mem:issuetrackerdb
   user: sa password: (empty)

## Default accounts (created on startup)
- admin / adminpass (ROLE_ADMIN,ROLE_DEVELOPER)
- dev / devpass (ROLE_DEVELOPER)
- reporter / reporterpass (ROLE_REPORTER)

## Auth flow
- POST /api/auth/login  {"username":"admin","password":"adminpass"}
- Copy token from response and add header: Authorization: Bearer <token>

## Example endpoints
- GET /api/issues?page=0&size=10&sort=updatedAt,desc
- POST /api/issues  (authenticated)
- POST /api/comments/issue/{issueId} (authenticated)
- GET /api/activities/issue/{issueId}

Notes:
- The JWT secret in application.properties must be changed before production.
- For submission, this project is intentionally backed by H2 for zero configuration.
