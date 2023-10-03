package com.kpmg.cacm.api.service.impl;

import java.util.Date;
import java.util.List;

import com.kpmg.cacm.api.dto.constant.ScheduleFrequency;
import com.kpmg.cacm.api.dto.constant.ScheduleType;
import com.kpmg.cacm.api.model.Category;
import com.kpmg.cacm.api.model.ExceptionDefinition;
import com.kpmg.cacm.api.repository.spring.ExceptionDefinitionRepository;
import com.kpmg.cacm.api.service.ExceptionDefinitionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class ExceptionDefinitionServiceImpl implements ExceptionDefinitionService {

    private final ExceptionDefinitionRepository exceptionDefinitionRepository;

    @Autowired
    public ExceptionDefinitionServiceImpl(final ExceptionDefinitionRepository exceptionDefinitionRepository) {
        this.exceptionDefinitionRepository = exceptionDefinitionRepository;
    }

    @Override
    @Transactional(rollbackFor = java.lang.Exception.class)
    public void save(final ExceptionDefinition exceptionDefinition) {
        final ExceptionDefinition savedEntity = this.exceptionDefinitionRepository.save(exceptionDefinition);
        savedEntity.getExceptionSchedule().setExceptionDefinitionId(savedEntity.getId());
        this.exceptionDefinitionRepository.save(savedEntity);
    }

    @Override
    public ExceptionDefinition findById(final Long id) {
        return this.exceptionDefinitionRepository.findByIdAndDeletedFalse(id);
    }

    @Override
    public List<ExceptionDefinition> findAllByCategories(List<Category> categories, Long ownerId) {
        return this.exceptionDefinitionRepository.findAllByCategoryInAndDeletedFalse(
            categories, ownerId
        );
    }

    @Override
    public List<ExceptionDefinition> findAllForScheduledRun(final ScheduleFrequency scheduleFrequency) {
        return this.exceptionDefinitionRepository.findAllForRun(ScheduleType.AUTOMATIC, scheduleFrequency);
    }

    @Override
    public List<ExceptionDefinition> findAllForManualRun() {
        return this.exceptionDefinitionRepository.findAllForRun(ScheduleType.MANUAL, null);
    }

    @Override
    public void deleteById(final Long id) {
        final ExceptionDefinition exceptionDefinition = this.findById(id);
        exceptionDefinition.setDeleted(true);
        exceptionDefinition.setDeletionToken(new Date().getTime());
        this.save(exceptionDefinition);
    }

    @Override
    public long countAllByCategories(List<Category> categories) {
        return this.exceptionDefinitionRepository.countAllByCategoryInAndDeletedFalse(categories);
    }

}
