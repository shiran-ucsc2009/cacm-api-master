package com.kpmg.cacm.api.service.impl;

import java.util.List;

import com.kpmg.cacm.api.model.AllowedTable;
import com.kpmg.cacm.api.repository.spring.AllowedTableRepository;
import com.kpmg.cacm.api.service.AllowedTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AllowedTableServiceImpl implements AllowedTableService {

    private final AllowedTableRepository allowedTableRepository;

    @Autowired
    public AllowedTableServiceImpl(AllowedTableRepository allowedTableRepository) {
        this.allowedTableRepository = allowedTableRepository;
    }

    @Override
    public List<AllowedTable> findAll() {
        return this.allowedTableRepository.findAllByDeletedFalse();
    }

    @Override
    public String encrypt(String name) {
        return name;
    }
}

