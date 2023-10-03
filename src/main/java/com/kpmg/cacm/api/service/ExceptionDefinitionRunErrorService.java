package com.kpmg.cacm.api.service;

import java.util.Date;
import java.util.List;

import com.kpmg.cacm.api.model.ExceptionDefinitionRunError;

public interface ExceptionDefinitionRunErrorService {

    void save(ExceptionDefinitionRunError expDefRunError);

    List<ExceptionDefinitionRunError> findAllByCreationTimestampAfter(final Date createdAfter);

}
