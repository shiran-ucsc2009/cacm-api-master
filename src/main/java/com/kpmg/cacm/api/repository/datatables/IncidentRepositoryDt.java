package com.kpmg.cacm.api.repository.datatables;

import com.kpmg.cacm.api.model.Incident;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IncidentRepositoryDt extends DataTablesRepository<Incident, Long> {
}
