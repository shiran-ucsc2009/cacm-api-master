package com.kpmg.cacm.api.repository.spring;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.kpmg.cacm.api.dto.constant.ScheduleFrequency;
import com.kpmg.cacm.api.dto.constant.ScheduleType;
import com.kpmg.cacm.api.model.Category;
import com.kpmg.cacm.api.model.ExceptionDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ExceptionDefinitionRepository extends JpaRepository<ExceptionDefinition, Long> {

    ExceptionDefinition findByIdAndDeletedFalse(Long id);

    @Query("SELECT e FROM ExceptionDefinition e WHERE e.category IN :categories AND (:ownerId IS NULL OR e.owner.id = :ownerId) AND e.deleted = false")
    List<ExceptionDefinition> findAllByCategoryInAndDeletedFalse(@Param("categories") List<Category> categories, @Param("ownerId") Long ownerId);

    long countAllByCategoryInAndDeletedFalse(List<Category> category);

    @Query("SELECT ed FROM ExceptionDefinition ed WHERE ed.deleted=FALSE AND ed.active=TRUE AND ed.exceptionSchedule.scheduleType=:scheduleType AND (:scheduleFrequency IS NULL OR ed.exceptionSchedule.scheduleFrequency=:scheduleFrequency)")
    List<ExceptionDefinition> findAllForRun(@NotNull @Param("scheduleType") ScheduleType scheduleType, @Param("scheduleFrequency") ScheduleFrequency scheduleFrequency);
}
