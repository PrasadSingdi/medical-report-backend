# Patients API

Base URL: `http://localhost:8080/api/patients`

Requests use `PatientRequest`; responses use `PatientResponse`. Dates are ISO-8601 date-time strings. `ownerType` is `PATIENT` or `HOSPITAL` and defaults to `PATIENT`.

| Method | Path | Description |
| --- | --- | --- |
| GET | `/api/patients` | List patients |
| GET | `/api/patients/{id}` | Fetch one patient by UUID |
| POST | `/api/patients` | Create patient; `firstName` and `lastName` are required |
| PUT | `/api/patients/{id}` | Update patient details |
| DELETE | `/api/patients/{id}` | Delete patient |

Example:

```json
{"firstName":"Ravi","lastName":"Kumar","patientType":"Mr.","age":"42","gender":"M","mobileNumber":"9999999999","sampleType":"WB-EDTA","reqNo":"REQ-001","ownerType":"HOSPITAL"}
```

Create returns `201`; delete returns `204`; missing IDs return `404`; validation errors return `400`. Changes to fields, validation, or service behavior must update this document and patient unit tests.
