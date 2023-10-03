package com.kpmg.cacm.api.service;

import java.util.List;

import com.kpmg.cacm.api.model.ExceptionSchedule;

public interface ExceptionScheduleService {

    ExceptionSchedule save(ExceptionSchedule exceptionSchedule);

    ExceptionSchedule findById(Long id);

    List<ExceptionSchedule> findAll();

    long countAll();

}
