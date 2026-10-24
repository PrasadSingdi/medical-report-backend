# Reports API

Base URL: `http://localhost:8080/api/reports`

The request and response follows the Angular `MedicalReport` model: `id`, `patientInfo`, `tests`, optional `comments`, and `createdDate`. Create stores and links a patient record and preserves patient/test snapshots in the report. Set `patientInfo.ownerType` to `PATIENT` or `HOSPITAL` for the recipient.

| Method | Path | Description |
| --- | --- | --- |
| GET | `/api/reports` | List reports |
| GET | `/api/reports/{id}` | Fetch one report |
| POST | `/api/reports` | Create report and patient record |
| PUT | `/api/reports/{id}` | Update report and linked patient |
| DELETE | `/api/reports/{id}` | Delete report |

The UI sends selected test definitions with observed values. Report text is sanitized before persistence; database access uses Spring Data JPA. Create returns `201`; delete returns `204`; missing IDs return `404`. Changes to report persistence, patient linkage, snapshots, or business rules must update this document and report service unit tests.
