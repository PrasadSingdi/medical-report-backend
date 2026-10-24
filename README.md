# Medical Report Backend

Standalone Spring Boot REST API for the Apex Diagnostic Centre Angular application. Runs on `http://localhost:8080`; PostgreSQL stores patient, report, and laboratory test data. See `docs/` for the project summary and endpoint contracts.

Run with Java 17 and Maven 3.9+: `mvn spring-boot:run`. Set `DATABASE_URL`, `DATABASE_USERNAME`, and `DATABASE_PASSWORD` to configure PostgreSQL. Local defaults use `jdbc:postgresql://localhost:5432/reports`, `postgres` / `postgres`.

To start PostgreSQL and the API together, run `docker compose up --build` from this directory.

Run backend unit tests with `mvn test`.

The Angular app runs separately at `http://localhost:4200`. CORS allows that origin by default; override with `CORS_ORIGIN`.

Spring Security's default authentication is active, but the Angular app does not yet have a login flow or token support. Authentication/authorization integration (OTP, Google, JWT, owner/read/write access) remains future work, so the UI cannot call protected routes until it is implemented. Keep the API on a trusted local network.
