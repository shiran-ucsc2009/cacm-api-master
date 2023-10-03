package com.kpmg.cacm.api.repository.spring;

import java.util.List;

import com.kpmg.cacm.api.dto.constant.ExceptionStatus;
import com.kpmg.cacm.api.model.Category;
import com.kpmg.cacm.api.model.Exception;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ExceptionRepository extends JpaRepository<Exception, Long> {

    @Query("SELECT e FROM Exception e WHERE e.exceptionDefinitionRun.category IN :categories AND (:ownerId IS NULL OR e.exceptionDefinitionRun.owner.id = :ownerId) AND e.deleted = false AND (COALESCE(:statuses, NULL) IS NULL OR e.status IN :statuses)")
    List<Exception> findAllByCategoryInAndOwner_IdAndStatusInAndDeletedFalse(@Param("categories") List<Category> categories, @Param("ownerId") Long ownerId, @Param("statuses") List<ExceptionStatus> statuses);

    Exception findAllByIdAndDeletedFalse(Long id);
}
