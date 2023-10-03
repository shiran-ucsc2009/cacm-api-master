package com.kpmg.cacm.api.service.impl;

import com.kpmg.cacm.api.model.EmailProperty;
import com.kpmg.cacm.api.repository.spring.EmailPropertyRepository;
import com.kpmg.cacm.api.service.EmailPropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailPropertyServiceImpl implements EmailPropertyService {

    private final EmailPropertyRepository emailPropertyRepository;

    @Autowired
    public EmailPropertyServiceImpl(final EmailPropertyRepository emailPropertyRepository) {
        this.emailPropertyRepository = emailPropertyRepository;
    }

    @Override
    public EmailProperty findByName(final String name) {
        return this.emailPropertyRepository.findByNameAndDeletedFalse(name);
    }
}
