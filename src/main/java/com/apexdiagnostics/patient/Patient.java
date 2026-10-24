package com.apexdiagnostics.patient;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

public class Patient {
    private UUID id = UUID.randomUUID();
    private String firstName;
    private String middleName;
    private String lastName;
    private String patientType;
    private String age;
    private String gender;
    private String mobileNumber;
    private String vialId;
    private String refDoctor;
    private String clientName;
    private String clientCode;
    private LocalDateTime collectedOn;
    private LocalDateTime registeredOn;
    private LocalDateTime reportedOn;
    private String sampleType;
    private String reqNo;
    private String ownerType = "PATIENT";
    private final Instant createdAt = Instant.now();

    protected Patient() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String v) {
        firstName = v;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String v) {
        middleName = v;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String v) {
        lastName = v;
    }

    public String getPatientType() {
        return patientType;
    }

    public void setPatientType(String v) {
        patientType = v;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String v) {
        age = v;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String v) {
        gender = v;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String v) {
        mobileNumber = v;
    }

    public String getVialId() {
        return vialId;
    }

    public void setVialId(String v) {
        vialId = v;
    }

    public String getRefDoctor() {
        return refDoctor;
    }

    public void setRefDoctor(String v) {
        refDoctor = v;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String v) {
        clientName = v;
    }

    public String getClientCode() {
        return clientCode;
    }

    public void setClientCode(String v) {
        clientCode = v;
    }

    public LocalDateTime getCollectedOn() {
        return collectedOn;
    }

    public void setCollectedOn(LocalDateTime v) {
        collectedOn = v;
    }

    public LocalDateTime getRegisteredOn() {
        return registeredOn;
    }

    public void setRegisteredOn(LocalDateTime v) {
        registeredOn = v;
    }

    public LocalDateTime getReportedOn() {
        return reportedOn;
    }

    public void setReportedOn(LocalDateTime v) {
        reportedOn = v;
    }

    public String getSampleType() {
        return sampleType;
    }

    public void setSampleType(String v) {
        sampleType = v;
    }

    public String getReqNo() {
        return reqNo;
    }

    public void setReqNo(String v) {
        reqNo = v;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String v) {
        ownerType = v;
    }
}
