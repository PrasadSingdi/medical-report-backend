package com.apexdiagnostics.report;

import com.apexdiagnostics.common.InputSanitizer;
import com.apexdiagnostics.common.ResourceNotFoundException;
import com.apexdiagnostics.patient.PatientRequest;
import com.apexdiagnostics.patient.PatientService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class MedicalReportService {
    private final MedicalReportRepository reports;
    private final PatientService patients;
    private final ObjectMapper mapper;
    private final InputSanitizer sanitizer;

    public MedicalReportService(MedicalReportRepository reports, PatientService patients, ObjectMapper mapper, InputSanitizer sanitizer) {
        this.reports = reports;
        this.patients = patients;
        this.mapper = mapper;
        this.sanitizer = sanitizer;
    }

    public List<JsonNode> all() {
        return reports.findAll().stream().map(this::read).toList();
    }

    public JsonNode get(String id) {
        return read(reports.findById(id).orElseThrow(() -> new ResourceNotFoundException("Report not found: " + id)));
    }

    @Transactional
    public JsonNode create(JsonNode body) {
        String id = body.path("id").asText();
        if (id.isBlank()) id = UUID.randomUUID().toString();
        if (reports.existsById(id)) throw new IllegalArgumentException("Report already exists: " + id);
        JsonNode safe = sanitize(body);
        UUID patientId = patients.create(patientRequest(safe.path("patientInfo"))).getId();
        return save(id, patientId, safe);
    }

    @Transactional
    public JsonNode update(String id, JsonNode body) {
        var previous = reports.findById(id).orElseThrow(() -> new ResourceNotFoundException("Report not found: " + id));
        JsonNode safe = sanitize(body);
        UUID patientId = previous.getPatientId();
        if (patientId == null) patientId = patients.create(patientRequest(safe.path("patientInfo"))).getId();
        else patients.update(patientId, patientRequest(safe.path("patientInfo")));
        return save(id, patientId, safe);
    }

    public void delete(String id) {
        if (!reports.existsById(id)) throw new ResourceNotFoundException("Report not found: " + id);
        reports.deleteById(id);
    }

    private JsonNode save(String id, UUID patientId, JsonNode data) {
        try {
            ((com.fasterxml.jackson.databind.node.ObjectNode) data).put("id", id);
            var e = new MedicalReportEntity();
            e.setId(id);
            e.setPatientId(patientId);
            e.setReportData(mapper.writeValueAsString(data));
            return read(reports.save(e));
        } catch (Exception ex) {
            throw new IllegalArgumentException("Invalid report payload", ex);
        }
    }

    private JsonNode read(MedicalReportEntity e) {
        try {
            return mapper.readTree(e.getReportData());
        } catch (Exception ex) {
            throw new IllegalStateException("Could not read stored report", ex);
        }
    }

    private JsonNode sanitize(JsonNode input) {
        JsonNode copy = input.deepCopy();
        sanitizeNode(copy);
        return copy;
    }

    private void sanitizeNode(JsonNode node) {
        if (node.isObject()) node.properties().forEach(entry -> {
            if (entry.getValue().isTextual())
                ((com.fasterxml.jackson.databind.node.ObjectNode) node).put(entry.getKey(), sanitizer.clean(entry.getValue().asText()));
            else sanitizeNode(entry.getValue());
        });
        else if (node.isArray()) node.forEach(this::sanitizeNode);
    }

    private PatientRequest patientRequest(JsonNode p) {
        return new PatientRequest(p.path("firstName").asText(), text(p, "middleName"), p.path("lastName").asText(), text(p, "patientType"), text(p, "age"), text(p, "gender"), text(p, "mobileNumber"), text(p, "vialId"), text(p, "refDoctor"), text(p, "clientName"), text(p, "clientCode"), date(p, "collectedOn"), date(p, "registeredOn"), date(p, "reportedOn"), text(p, "sampleType"), text(p, "reqNo"), text(p, "ownerType"));
    }

    private String text(JsonNode n, String field) {
        return n.hasNonNull(field) ? n.get(field).asText() : null;
    }

    private java.time.LocalDateTime date(JsonNode n, String field) {
        try {
            if (!n.hasNonNull(field)) return null;
            String value = n.get(field).asText();
            try {
                return java.time.LocalDateTime.parse(value);
            } catch (Exception ignored) {
                return java.time.OffsetDateTime.parse(value).toLocalDateTime();
            }
        } catch (Exception ex) {
            throw new IllegalArgumentException("Invalid date for " + field, ex);
        }
    }
}
