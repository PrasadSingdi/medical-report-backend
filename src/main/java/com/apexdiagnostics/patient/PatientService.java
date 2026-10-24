package com.apexdiagnostics.patient;

import com.apexdiagnostics.common.InputSanitizer;
import com.apexdiagnostics.common.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PatientService {
    private final PatientRepository repository;
    private final InputSanitizer sanitizer;

    public PatientService(PatientRepository repository, InputSanitizer sanitizer) {
        this.repository = repository;
        this.sanitizer = sanitizer;
    }

    public List<Patient> findAll() {
        return repository.findAll();
    }

    public Patient find(UUID id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Patient not found: " + id));
    }

    public Patient create(PatientRequest request) {
        return repository.save(apply(new Patient(), request));
    }

    public Patient update(UUID id, PatientRequest request) {
        return repository.save(apply(find(id), request));
    }

    public void delete(UUID id) {
        if (!repository.existsById(id)) throw new ResourceNotFoundException("Patient not found: " + id);
        repository.deleteById(id);
    }

    private Patient apply(Patient p, PatientRequest r) {
        p.setFirstName(sanitizer.requireClean(r.firstName(), "firstName"));
        p.setMiddleName(sanitizer.clean(r.middleName()));
        p.setLastName(sanitizer.requireClean(r.lastName(), "lastName"));
        p.setPatientType(sanitizer.clean(r.patientType()));
        p.setAge(sanitizer.clean(r.age()));
        p.setGender(sanitizer.clean(r.gender()));
        p.setMobileNumber(sanitizer.clean(r.mobileNumber()));
        p.setVialId(sanitizer.clean(r.vialId()));
        p.setRefDoctor(sanitizer.clean(r.refDoctor()));
        p.setClientName(sanitizer.clean(r.clientName()));
        p.setClientCode(sanitizer.clean(r.clientCode()));
        p.setCollectedOn(r.collectedOn());
        p.setRegisteredOn(r.registeredOn());
        p.setReportedOn(r.reportedOn());
        p.setSampleType(sanitizer.clean(r.sampleType()));
        p.setReqNo(sanitizer.clean(r.reqNo()));
        p.setOwnerType(r.ownerType() == null ? "PATIENT" : sanitizer.clean(r.ownerType()));
        return p;
    }
}
