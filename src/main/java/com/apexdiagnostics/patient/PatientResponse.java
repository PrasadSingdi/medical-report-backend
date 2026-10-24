package com.apexdiagnostics.patient;

import java.util.UUID;

public record PatientResponse(UUID id, String firstName, String middleName, String lastName, String patientType,
                              String age, String gender, String mobileNumber, String vialId, String refDoctor,
                              String clientName, String clientCode, java.time.LocalDateTime collectedOn,
                              java.time.LocalDateTime registeredOn, java.time.LocalDateTime reportedOn,
                              String sampleType, String reqNo, String ownerType) {
    public static PatientResponse from(Patient p) {
        return new PatientResponse(p.getId(), p.getFirstName(), p.getMiddleName(), p.getLastName(), p.getPatientType(), p.getAge(), p.getGender(), p.getMobileNumber(), p.getVialId(), p.getRefDoctor(), p.getClientName(), p.getClientCode(), p.getCollectedOn(), p.getRegisteredOn(), p.getReportedOn(), p.getSampleType(), p.getReqNo(), p.getOwnerType());
    }
}
