package com.apexdiagnostics.report;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class MedicalReportController {
    private final MedicalReportService service;

    public MedicalReportController(MedicalReportService service) {
        this.service = service;
    }

    @GetMapping
    public List<JsonNode> all() {
        return service.all();
    }

    @GetMapping("/{id}")
    public JsonNode get(@PathVariable String id) {
        return service.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public JsonNode create(@RequestBody JsonNode body) {
        return service.create(body);
    }

    @PutMapping("/{id}")
    public JsonNode update(@PathVariable String id, @RequestBody JsonNode body) {
        return service.update(id, body);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        service.delete(id);
    }
}
