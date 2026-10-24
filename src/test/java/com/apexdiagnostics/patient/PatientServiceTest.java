package com.apexdiagnostics.patient;

import com.apexdiagnostics.common.InputSanitizer;
import com.apexdiagnostics.common.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {
    @Mock
    PatientRepository repository;
    PatientService service;

    @BeforeEach
    void setUp() {
        service = new PatientService(repository, new InputSanitizer());
    }

    private PatientRequest request(String first, String last) {
        return new PatientRequest(first, null, last, "Mr.", "40", "M", null, null, null, null, null, null, null, null, null, null, "PATIENT");
    }

    @Test
    void findAllReturnsRepositoryPatients() {
        var patients = List.of(new Patient());
        when(repository.findAll()).thenReturn(patients);
        assertSame(patients, service.findAll());
    }

    @Test
    void findReturnsPatientWhenPresent() {
        var patient = new Patient();
        when(repository.findById(patient.getId())).thenReturn(Optional.of(patient));
        assertSame(patient, service.find(patient.getId()));
    }

    @Test
    void findThrowsWhenPatientIsMissing() {
        var id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.find(id));
    }

    @Test
    void createSanitizesNamesBeforeSaving() {
        when(repository.save(any(Patient.class))).thenAnswer(call -> call.getArgument(0));
        service.create(request("<b>Alice</b>", "Jones"));
        var captor = ArgumentCaptor.forClass(Patient.class);
        verify(repository).save(captor.capture());
        assertEquals("Alice", captor.getValue().getFirstName());
        assertEquals("PATIENT", captor.getValue().getOwnerType());
    }

    @Test
    void updateChangesExistingPatient() {
        var patient = new Patient();
        when(repository.findById(patient.getId())).thenReturn(Optional.of(patient));
        when(repository.save(any(Patient.class))).thenAnswer(call -> call.getArgument(0));
        var result = service.update(patient.getId(), request("Updated", "Name"));
        assertSame(patient, result);
        assertEquals("Updated", result.getFirstName());
    }

    @Test
    void deleteRemovesExistingPatient() {
        var id = UUID.randomUUID();
        when(repository.existsById(id)).thenReturn(true);
        service.delete(id);
        verify(repository).deleteById(id);
    }

    @Test
    void deleteThrowsForMissingPatient() {
        var id = UUID.randomUUID();
        when(repository.existsById(id)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> service.delete(id));
        verify(repository, never()).deleteById(id);
    }
}
