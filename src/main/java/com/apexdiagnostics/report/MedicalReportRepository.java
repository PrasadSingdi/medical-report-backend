package com.apexdiagnostics.report;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicalReportRepository extends JpaRepository<MedicalReportEntity, String> {
}
