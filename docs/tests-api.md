# Laboratory Tests API

Base URL: `http://localhost:8080/api/tests`

The JSON body mirrors the Angular `Test` model (`id`, `name`, `category`, optional method/unit/reference interval, and `subTests`). Each sub-test holds its method, unit, and biological reference interval. Reports store a snapshot, so catalogue edits do not change saved report results.

| Method | Path | Description |
| --- | --- | --- |
| GET | `/api/tests` | List tests |
| GET | `/api/tests/{id}` | Fetch one test |
| POST | `/api/tests` | Create test; ID is required and unique |
| PUT | `/api/tests/{id}` | Update test definition |
| DELETE | `/api/tests/{id}` | Delete test |

Create returns `201`; delete returns `204`; duplicate IDs and malformed payloads return `400`; missing IDs return `404`. Text fields are sanitized, and persistence uses JPA instead of SQL assembled from request data. Changes to the contract or catalogue behavior must update this document and catalogue unit tests.
