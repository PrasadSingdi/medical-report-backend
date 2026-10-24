package com.apexdiagnostics.report;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "medical_reports")
public class MedicalReportEntity {
    @Id
    private String id;
    private UUID patientId;
    @Column(columnDefinition = "text", nullable = false)
    private String reportData;
    private final Instant createdDate = Instant.now();

    protected MedicalReportEntity() {
    }

    public String getId() {
        return id;
    }

    public void setId(String v) {
        id = v;
    }

    public UUID getPatientId() {
        return patientId;
    }

    public void setPatientId(UUID v) {
        patientId = v;
    }

    public String getReportData() {
        return reportData;
    }

    public void setReportData(String v) {
        reportData = v;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }
}
