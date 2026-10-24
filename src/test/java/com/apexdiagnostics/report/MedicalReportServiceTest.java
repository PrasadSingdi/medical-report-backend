package com.apexdiagnostics.report;

import com.apexdiagnostics.common.InputSanitizer;
import com.apexdiagnostics.common.ResourceNotFoundException;
import com.apexdiagnostics.patient.Patient;
import com.apexdiagnostics.patient.PatientRequest;
import com.apexdiagnostics.patient.PatientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedicalReportServiceTest {
    @Mock
    MedicalReportRepository reports;
    @Mock
    PatientService patients;
    MedicalReportService service;
    ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
        service = new MedicalReportService(reports, patients, mapper, new InputSanitizer());
    }

    private String payload() {
        return "{\"id\":\"R-1\",\"patientInfo\":{\"firstName\":\"<b>Ravi</b>\",\"lastName\":\"Kumar\",\"collectedOn\":\"2026-01-01T10:00:00Z\"},\"tests\":[{\"name\":\"CBC\"}],\"createdDate\":\"2026-01-01T10:00:00Z\"}";
    }

    @Test
    void allReturnsStoredReports() throws Exception {
        var entity = new MedicalReportEntity();
        entity.setReportData(payload());
        when(reports.findAll()).thenReturn(List.of(entity));
        assertEquals("R-1", service.all().get(0).path("id").asText());
    }

    @Test
    void getReturnsReportWhenFound() throws Exception {
        var entity = new MedicalReportEntity();
        entity.setReportData(payload());
        when(reports.findById("R-1")).thenReturn(Optional.of(entity));
        assertEquals("R-1", service.get("R-1").path("id").asText());
    }

    @Test
    void getThrowsWhenReportIsMissing() {
        when(reports.findById("missing")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.get("missing"));
    }

    @Test
    void createSanitizesSnapshotAndPersistsPatientLink() throws Exception {
        var patient = mock(Patient.class);
        var patientId = UUID.randomUUID();
        when(patient.getId()).thenReturn(patientId);
        when(patients.create(any(PatientRequest.class))).thenReturn(patient);
        when(reports.existsById("R-1")).thenReturn(false);
        when(reports.save(any(MedicalReportEntity.class))).thenAnswer(call -> call.getArgument(0));
        var response = service.create(mapper.readTree(payload()));
        var captor = ArgumentCaptor.forClass(MedicalReportEntity.class);
        verify(reports).save(captor.capture());
        assertEquals(patientId, captor.getValue().getPatientId());
        assertEquals("Ravi", response.path("patientInfo").path("firstName").asText());
        verify(patients).create(any(PatientRequest.class));
    }

    @Test
    void createRejectsDuplicateReportId() throws Exception {
        when(reports.existsById("R-1")).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> service.create(mapper.readTree(payload())));
        verify(reports, never()).save(any());
    }

    @Test
    void updatePersistsReportAndUpdatesLinkedPatient() throws Exception {
        var existing = new MedicalReportEntity();
        existing.setId("R-1");
        var patientId = UUID.randomUUID();
        existing.setPatientId(patientId);
        existing.setReportData(payload());
        when(reports.findById("R-1")).thenReturn(Optional.of(existing));
        when(reports.save(any(MedicalReportEntity.class))).thenAnswer(call -> call.getArgument(0));
        var updated = service.update("R-1", mapper.readTree(payload()));
        assertEquals("R-1", updated.path("id").asText());
        verify(patients).update(eq(patientId), any(PatientRequest.class));
    }

    @Test
    void updateThrowsWhenReportIsMissing() throws Exception {
        when(reports.findById("missing")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.update("missing", mapper.readTree(payload())));
    }

    @Test
    void deleteRemovesExistingReport() {
        when(reports.existsById("R-1")).thenReturn(true);
        service.delete("R-1");
        verify(reports).deleteById("R-1");
    }

    @Test
    void deleteThrowsForMissingReport() {
        when(reports.existsById("missing")).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> service.delete("missing"));
        verify(reports, never()).deleteById("missing");
    }
}
