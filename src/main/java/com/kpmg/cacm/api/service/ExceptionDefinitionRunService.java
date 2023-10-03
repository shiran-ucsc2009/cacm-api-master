package com.kpmg.cacm.api.service;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kpmg.cacm.api.model.ExceptionDefinitionRun;

public interface ExceptionDefinitionRunService {

    ExceptionDefinitionRun save(ExceptionDefinitionRun category);

    ExceptionDefinitionRun findById(Long id);

    List<ExceptionDefinitionRun> findAll();

    long countAll();

    void run(ExceptionDefinitionRun exceptionDefRun, String traceId) throws JsonProcessingException;

}
