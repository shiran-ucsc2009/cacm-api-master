package com.kpmg.cacm.api.service;

import java.util.List;

import com.kpmg.cacm.api.dto.constant.ScheduleFrequency;
import com.kpmg.cacm.api.model.Category;
import com.kpmg.cacm.api.model.ExceptionDefinition;

public interface ExceptionDefinitionService {

    void save(ExceptionDefinition exceptiondefinition);

    ExceptionDefinition findById(Long id);

    List<ExceptionDefinition> findAllByCategories(List<Category> categories, Long ownerId);

    List<ExceptionDefinition> findAllForScheduledRun(ScheduleFrequency scheduleFrequency);

    List<ExceptionDefinition> findAllForManualRun();

    void deleteById(Long id);

    long countAllByCategories(List<Category> categories);

}
