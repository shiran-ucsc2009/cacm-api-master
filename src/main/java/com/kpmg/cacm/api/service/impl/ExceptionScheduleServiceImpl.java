package com.kpmg.cacm.api.service.impl;

import java.util.List;

import com.kpmg.cacm.api.model.ExceptionSchedule;
import com.kpmg.cacm.api.repository.spring.ExceptionScheduleRepository;
import com.kpmg.cacm.api.service.ExceptionScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExceptionScheduleServiceImpl implements ExceptionScheduleService {

    private final ExceptionScheduleRepository exceptionScheduleRepository;

    @Autowired
    public ExceptionScheduleServiceImpl(final ExceptionScheduleRepository exceptionScheduleRepository) {
        this.exceptionScheduleRepository = exceptionScheduleRepository;
    }

    @Override
    public ExceptionSchedule save(final ExceptionSchedule exceptionSchedule) {
        return this.exceptionScheduleRepository.save(exceptionSchedule);
    }

    @Override
    public ExceptionSchedule findById(final Long id) {
        return this.exceptionScheduleRepository.findByIdAndDeletedFalse(id);
    }

    @Override
    public List<ExceptionSchedule> findAll() {
        return this.exceptionScheduleRepository.findAllByDeletedFalse();
    }

    @Override
    public long countAll() {
        return this.exceptionScheduleRepository.countAllByDeletedFalse();
    }
}
