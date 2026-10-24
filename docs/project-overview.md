# Medical Report Backend Overview

## Purpose

This standalone Spring Boot application provides REST APIs for diagnostic-centre patient records, laboratory test definitions, and reports, backed by PostgreSQL. It runs separately from the Angular UI at `http://localhost:8080`.

## Business scenarios

- Walk-in patient: record patient and sample data, choose tests, enter results, then save the report for the patient.
- Hospital referral: record the referred patient and sample data, choose tests, then save the report with `ownerType: HOSPITAL` for delivery to the hospital.

Reports retain a snapshot of patient details and selected test results. The patient record is also stored and linked by `patientId`.

## Layers

`controller` handles HTTP; `service` contains business rules and orchestration; `repository` uses Spring Data JPA; `entity` maps PostgreSQL records. `common` provides input sanitization, correlation IDs, and global error handling.

## Local configuration

- API: `http://localhost:8080`
- UI: `http://localhost:4200`
- Database: PostgreSQL database `reports` on `localhost:5432`
- `DATABASE_URL`, `DATABASE_USERNAME`, `DATABASE_PASSWORD`: database connection
- `CORS_ORIGIN`: allowed UI origin, defaults to `http://localhost:4200`
- `JWT_SECRET`: reserved configuration; JWT authentication is not implemented

Run `mvn spring-boot:run` here with Java 17 and Maven 3.9+. Hibernate schema update is for local development; production requires migrations.

## Current scope and limitations

Patient, report, and test domains are packages in one deployable application, not separate microservices. Authentication, OTP delivery, Google sign-in, password reset, and customer/owner API authorization remain unimplemented. Do not expose this API publicly until those protections are implemented.

## API documentation

- [Patients API](patients-api.md)
- [Reports API](reports-api.md)
- [Laboratory Tests API](tests-api.md)
