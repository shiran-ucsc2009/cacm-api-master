package com.kpmg.cacm.api.repository.datatables;

import com.kpmg.cacm.api.model.Exception;
import com.kpmg.cacm.api.model.ExceptionDefinitionRun;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExceptionRepositoryDt extends DataTablesRepository<Exception, Integer> {
}
