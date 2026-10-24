package com.apexdiagnostics.catalog;

import com.apexdiagnostics.common.InputSanitizer;
import com.apexdiagnostics.common.ResourceNotFoundException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tests")
public class LabTestController {
    private final LabTestRepository repository;
    private final ObjectMapper mapper;
    private final InputSanitizer sanitizer;

    public LabTestController(LabTestRepository repository, ObjectMapper mapper, InputSanitizer sanitizer) {
        this.repository = repository;
        this.mapper = mapper;
        this.sanitizer = sanitizer;
    }

    @GetMapping
    public List<JsonNode> all() {
        return repository.findAll().stream().map(this::json).toList();
    }

    @GetMapping("/{id}")
    public JsonNode get(@PathVariable String id) {
        return json(repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Test not found: " + id)));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public JsonNode create(@RequestBody JsonNode body) {
        String id = body.path("id").asText();
        if (id.isBlank()) throw new IllegalArgumentException("Test id is required");
        if (repository.existsById(id)) throw new IllegalArgumentException("Test already exists");
        return json(repository.save(entity(id, body)));
    }

    @PutMapping("/{id}")
    public JsonNode update(@PathVariable String id, @RequestBody JsonNode body) {
        if (!repository.existsById(id)) throw new ResourceNotFoundException("Test not found: " + id);
        return json(repository.save(entity(id, body)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        if (!repository.existsById(id)) throw new ResourceNotFoundException("Test not found: " + id);
        repository.deleteById(id);
    }

    private LabTest entity(String id, JsonNode body) {
        try {
            var e = new LabTest();
            e.setId(id);
            e.setName(sanitizer.clean(body.path("name").asText()));
            e.setCategory(sanitizer.clean(body.path("category").asText()));
            JsonNode safe = body.deepCopy();
            sanitizeNode(safe);
            e.setTestData(mapper.writeValueAsString(safe));
            return e;
        } catch (Exception ex) {
            throw new IllegalArgumentException("Invalid test payload", ex);
        }
    }

    private void sanitizeNode(JsonNode node) {
        if (node.isObject()) node.properties().forEach(entry -> {
            if (entry.getValue().isTextual())
                ((com.fasterxml.jackson.databind.node.ObjectNode) node).put(entry.getKey(), sanitizer.clean(entry.getValue().asText()));
            else sanitizeNode(entry.getValue());
        });
        else if (node.isArray()) node.forEach(this::sanitizeNode);
    }

    private JsonNode json(LabTest e) {
        try {
            return mapper.readTree(e.getTestData());
        } catch (Exception ex) {
            throw new IllegalStateException("Could not read stored test", ex);
        }
    }
}
