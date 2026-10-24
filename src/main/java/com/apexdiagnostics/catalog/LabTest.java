package com.apexdiagnostics.catalog;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "laboratory_tests")
public class LabTest {
    @Id
    private String id;
    private String name;
    private String category;
    @Column(columnDefinition = "text")
    private String testData;

    protected LabTest() {
    }

    public String getId() {
        return id;
    }

    public void setId(String v) {
        id = v;
    }

    public String getName() {
        return name;
    }

    public void setName(String v) {
        name = v;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String v) {
        category = v;
    }

    public String getTestData() {
        return testData;
    }

    public void setTestData(String v) {
        testData = v;
    }
}
