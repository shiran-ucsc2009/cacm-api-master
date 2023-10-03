package com.kpmg.cacm.api.repository.spring;

import com.kpmg.cacm.api.model.ReportingOfficer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportingOfficerRepository extends JpaRepository<ReportingOfficer, Long> {


    ReportingOfficer findByDeletedFalse();
}
