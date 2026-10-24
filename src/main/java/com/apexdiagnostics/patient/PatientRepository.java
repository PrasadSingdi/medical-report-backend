package com.apexdiagnostics.patient;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class PatientRepository {
    private final ConcurrentHashMap<UUID, Patient> cache = new ConcurrentHashMap<>();

    public List<Patient> findAll() {
        return List.copyOf(cache.values());
    }

    public Optional<Patient> findById(UUID id) {
        return Optional.ofNullable(cache.get(id));
    }

    public Patient save(Patient patient) {
        if (patient.getId() == null) patient.setId(UUID.randomUUID());
        cache.put(patient.getId(), patient);
        return patient;
    }

    public boolean existsById(UUID id) {
        return cache.containsKey(id);
    }

    public void deleteById(UUID id) {
        cache.remove(id);
    }
}
