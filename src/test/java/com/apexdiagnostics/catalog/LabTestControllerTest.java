package com.apexdiagnostics.catalog;

import com.apexdiagnostics.common.InputSanitizer;
import com.apexdiagnostics.common.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LabTestControllerTest {
    @Mock
    LabTestRepository repository;
    LabTestController controller;
    ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
        controller = new LabTestController(repository, mapper, new InputSanitizer());
    }

    private LabTest test(String id, String json) {
        var test = new LabTest();
        test.setId(id);
        test.setName("CBC");
        test.setCategory("HAEMATOLOGY");
        test.setTestData(json);
        return test;
    }

    @Test
    void allReturnsCatalogueDefinitions() {
        when(repository.findAll()).thenReturn(List.of(test("T-1", "{\"id\":\"T-1\",\"name\":\"CBC\"}")));
        assertEquals("T-1", controller.all().get(0).path("id").asText());
    }

    @Test
    void getReturnsDefinitionWhenFound() {
        when(repository.findById("T-1")).thenReturn(Optional.of(test("T-1", "{\"id\":\"T-1\"}")));
        assertEquals("T-1", controller.get("T-1").path("id").asText());
    }

    @Test
    void getThrowsWhenDefinitionIsMissing() {
        when(repository.findById("missing")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> controller.get("missing"));
    }

    @Test
    void createSanitizesAndPersistsDefinition() throws Exception {
        when(repository.existsById("T-2")).thenReturn(false);
        when(repository.save(any(LabTest.class))).thenAnswer(call -> call.getArgument(0));
        var body = mapper.readTree("{\"id\":\"T-2\",\"name\":\"<b>CBC</b>\",\"category\":\"HAEM\",\"subTests\":[]}");
        assertEquals("CBC", controller.create(body).path("name").asText());
        var captor = ArgumentCaptor.forClass(LabTest.class);
        verify(repository).save(captor.capture());
        assertEquals("CBC", captor.getValue().getName());
    }

    @Test
    void createRejectsMissingId() throws Exception {
        var body = mapper.readTree("{\"name\":\"CBC\"}");
        assertThrows(IllegalArgumentException.class, () -> controller.create(body));
        verify(repository, never()).save(any());
    }

    @Test
    void createRejectsDuplicateId() throws Exception {
        when(repository.existsById("T-1")).thenReturn(true);
        var body = mapper.readTree("{\"id\":\"T-1\",\"name\":\"CBC\"}");
        assertThrows(IllegalArgumentException.class, () -> controller.create(body));
    }

    @Test
    void updateReplacesExistingDefinition() throws Exception {
        when(repository.existsById("T-1")).thenReturn(true);
        when(repository.save(any(LabTest.class))).thenAnswer(call -> call.getArgument(0));
        var body = mapper.readTree("{\"id\":\"T-1\",\"name\":\"CBC updated\",\"category\":\"HAEM\"}");
        assertEquals("CBC updated", controller.update("T-1", body).path("name").asText());
    }

    @Test
    void updateThrowsForMissingDefinition() throws Exception {
        when(repository.existsById("missing")).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> controller.update("missing", mapper.readTree("{}")));
    }

    @Test
    void deleteRemovesExistingDefinition() {
        when(repository.existsById("T-1")).thenReturn(true);
        controller.delete("T-1");
        verify(repository).deleteById("T-1");
    }

    @Test
    void deleteThrowsForMissingDefinition() {
        when(repository.existsById("missing")).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> controller.delete("missing"));
        verify(repository, never()).deleteById("missing");
    }
}
