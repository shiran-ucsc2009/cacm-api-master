package com.kpmg.cacm.api.repository.spring;

import java.util.List;

import com.kpmg.cacm.api.model.ExceptionSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExceptionScheduleRepository extends JpaRepository<ExceptionSchedule, Long> {

    ExceptionSchedule findByIdAndDeletedFalse(Long id);

    List<ExceptionSchedule> findAllByDeletedFalse();

    long countAllByDeletedFalse();
}
