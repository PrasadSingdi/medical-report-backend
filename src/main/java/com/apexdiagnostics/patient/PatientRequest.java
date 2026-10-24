package com.apexdiagnostics.patient;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record PatientRequest(@NotBlank String firstName, String middleName, @NotBlank String lastName,
                             String patientType, String age, String gender, String mobileNumber, String vialId,
                             String refDoctor, String clientName, String clientCode, LocalDateTime collectedOn,
                             LocalDateTime registeredOn, LocalDateTime reportedOn, String sampleType, String reqNo,
                             String ownerType) {
}
