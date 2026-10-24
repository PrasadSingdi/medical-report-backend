package com.apexdiagnostics.catalog;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class LabTestRepository {
    private final ConcurrentHashMap<String, LabTest> tests = new ConcurrentHashMap<>();

    public List<LabTest> findAll() {
        return List.copyOf(tests.values());
    }

    public Optional<LabTest> findById(String id) {
        return Optional.ofNullable(tests.get(id));
    }

    public boolean existsById(String id) {
        return tests.containsKey(id);
    }

    public LabTest save(LabTest test) {
        tests.put(test.getId(), test);
        return test;
    }

    public void deleteById(String id) {
        tests.remove(id);
    }
}
