package com.kpmg.cacm.api.service.impl;

import java.util.Date;
import java.util.List;

import com.kpmg.cacm.api.model.ExceptionDefinitionRunError;
import com.kpmg.cacm.api.repository.spring.ExceptionDefinitionRunErrorRepository;
import com.kpmg.cacm.api.service.ExceptionDefinitionRunErrorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExceptionDefinitionRunErrorServiceImpl implements ExceptionDefinitionRunErrorService {

    private final ExceptionDefinitionRunErrorRepository expDefRunErrorRepository;

    @Autowired
    public ExceptionDefinitionRunErrorServiceImpl(final ExceptionDefinitionRunErrorRepository expDefRunErrorRepository) {
        this.expDefRunErrorRepository = expDefRunErrorRepository;
    }

    @Override
    public void save(final ExceptionDefinitionRunError expDefRunError) {
        this.expDefRunErrorRepository.save(expDefRunError);
    }

    @Override
    public List<ExceptionDefinitionRunError> findAllByCreationTimestampAfter(final Date createdAfter) {
        return this.expDefRunErrorRepository.findAllByCreationTimestampAfterAndDeletedFalse(createdAfter);
    }

}
