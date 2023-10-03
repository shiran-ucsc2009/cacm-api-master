package com.kpmg.cacm.api.repository.spring;

import com.kpmg.cacm.api.dto.constant.IncidentApproveAction;
import com.kpmg.cacm.api.dto.constant.IncidentStatus;
import com.kpmg.cacm.api.dto.constant.Priority;
import com.kpmg.cacm.api.dto.response.model.IncidentEmailDTO;
import com.kpmg.cacm.api.dto.response.model.IncidentGraphByOwnerDTO;
import com.kpmg.cacm.api.dto.response.model.RiskMatrixDTO;
import com.kpmg.cacm.api.dto.response.model.RiskMatrixExceptionDTO;
import com.kpmg.cacm.api.model.Category;
import com.kpmg.cacm.api.model.Exception;
import com.kpmg.cacm.api.model.Incident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, Long> {

    List<Incident> findAllByCategoryInAndDeletedFalse(List<Category> categories);

    Incident findByIdAndDeletedFalse(Long id);

    @Query("SELECT i FROM Incident i WHERE (COALESCE(:categories, NULL) IS NULL OR i.category IN :categories) AND (:exceptionId IS NULL OR i.exception.id=:exceptionId) AND (:customerId IS NULL OR i.clientCustomer.id=:customerId) AND (:customerRisk IS NULL OR i.clientCustomer.priority=:customerRisk) AND (COALESCE(:statuses, NULL) IS NULL OR i.status IN :statuses) AND (:assigneeId IS NULL OR i.assignee.id=:assigneeId) AND (:ownerId IS NULL OR i.exception.exceptionDefinitionRun.owner.id=:ownerId) AND i.deleted=false")
    List<Incident> findAllByCategoryInAndException_IdAndClientCustomer_IdAndStatusInAndDeletedFalse(@Param("categories") List<Category> categories, @Param("exceptionId") Long exceptionId, @Param("customerId") Long customerId, @Param("statuses") List<IncidentStatus> statuses,@Param("customerRisk") Priority customerRisk, @Param("assigneeId") Long assigneeId, @Param("ownerId") Long ownerId);

    @Query("SELECT COUNT(i) FROM Incident i WHERE (COALESCE(:categories, NULL) IS NULL OR i.category IN :categories) AND (:status IS NULL OR i.status=:status) AND (:customerId IS NULL OR i.clientCustomer.customerId=:customerId) AND (:assigneeId IS NULL OR i.assignee.id=:assigneeId) AND (:ownerId IS NULL OR i.exception.exceptionDefinitionRun.owner.id=:ownerId) AND i.deleted=false")
    long countAllByCategoryInAndStatusAndDeletedFalse(@Param("categories") List<Category> categories, @Param("status") IncidentStatus status, @Param("customerId") String customerId, @Param("assigneeId") Long assigneeId, @Param("ownerId") Long ownerId);

    List<Incident> findAllByExceptionAndDeletedFalse(Exception exception);

    @Query("SELECT i FROM Incident i WHERE (:status IS NULL OR i.status=:status) AND (COALESCE(:categories, NULL) IS NULL OR i.category IN :categories) AND (:assigneeId IS NULL OR i.assignee.id=:assigneeId) AND (:ownerId IS NULL OR i.exception.exceptionDefinitionRun.owner.id=:ownerId) AND i.deleted=false")
    List<Incident> findAllByStatusAndCategoryInAndDeletedFalse(@Param("status") IncidentStatus status, @Param("categories") List<Category> categories, @Param("assigneeId") Long assigneeId, @Param("ownerId") Long ownerId);

    List<Incident> findAllByStatusInAndCategoryInAndDeletedFalse(Collection<IncidentStatus> status, Collection<Category> category);

    Incident findIncidentByIdAndDeletedFalse(Long id);

    List<Incident> findAllByStatusAndCategoryInAndCreationTimestampBetweenAndDeletedFalse(@Param("status") IncidentStatus incidentStatus, @Param("categories") List<Category> categories, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

    long countByIncidentResolution_IncidentFeedback_ApproveActionAndCategoryAndDeletedFalse(IncidentApproveAction incidentApproveAction, Category categories);

    long countByStatusAndIncidentResolution_IncidentFeedback_ApproveActionAndCategoryAndDeletedFalse(Collection<IncidentStatus> status,IncidentApproveAction incidentApproveAction, Category categories);

    long countByStatusAndCategoryAndDeletedFalse(IncidentStatus incidentStatus, Category categories);

    long countByCategoryAndException_ExceptionDefinitionRun_PriorityAndDeletedFalse(Category categories, Priority priority);

    @Query("SELECT new com.kpmg.cacm.api.dto.response.model.IncidentGraphByOwnerDTO(i.exception.exceptionDefinitionRun.owner.id, COUNT(i.id), i.exception.exceptionDefinitionRun.owner.username, i.exception.exceptionDefinitionRun.priority) FROM Incident i WHERE (COALESCE(:categories, NULL) IS NULL OR i.category IN :categories) AND i.deleted=false GROUP BY i.exception.exceptionDefinitionRun.owner.username,i.exception.exceptionDefinitionRun.priority,i.exception.exceptionDefinitionRun.owner.id ORDER BY i.exception.exceptionDefinitionRun.owner.username")
    List<IncidentGraphByOwnerDTO> findIncidentGraphByOwnerByCategory(@Param("categories") List<Category> categories);

    @Query("SELECT new  com.kpmg.cacm.api.dto.response.model.RiskMatrixDTO(count(distinct i.id),i.exception.exceptionDefinitionRun.priority,i.clientCustomer.priority) from Incident i where i.deleted=false  AND (COALESCE(:statuses, NULL) IS NULL OR i.status IN :statuses) AND  (COALESCE(:categories, NULL) IS NULL OR i.category IN :categories)GROUP BY i.clientCustomer.priority,i.exception.exceptionDefinitionRun.priority ")
    List<RiskMatrixDTO> findRiskMatrixDTOByPriority(@Param("categories") List<Category> categories,@Param("statuses") List<IncidentStatus> incidentStatuses);

    @Query("SELECT new com.kpmg.cacm.api.dto.response.model.RiskMatrixExceptionDTO(i.exception.id,i.exception.exceptionDefinitionRun.name,i.exception.creationTimestamp,i.exception.exceptionDefinitionRun.owner.name,i.category.name,i.exception.exceptionDefinitionRun.priority,i.clientCustomer.priority,count(i.id)) from Incident i where i.exception.creationTimestamp > current_date and (COALESCE(:categories, NULL) IS NULL OR i.category IN :categories) and (:customerRisk IS NULL OR i.clientCustomer.priority=:customerRisk)  and (:scenarioRisk IS NULL OR i.exception.exceptionDefinitionRun.priority =:scenarioRisk)  and  i.deleted=false GROUP BY i.exception.id,i.exception.exceptionDefinitionRun.name,i.exception.creationTimestamp,i.exception.exceptionDefinitionRun.owner.name,i.category.name,i.exception.exceptionDefinitionRun.priority,i.clientCustomer.priority")
    List<RiskMatrixExceptionDTO> findRiskMatrixExceptionDTOByPriority(@Param("categories") List<Category> categories,@Param("customerRisk") Priority priority,@Param("scenarioRisk") Priority priorities);

    @Query("SELECT new com.kpmg.cacm.api.dto.response.model.IncidentEmailDTO(count(distinct i.id),i.category.name,i.assignee.id) from  Incident  i where i.deleted=false and i.creationTimestamp >= current_date group by i.assignee.id,i.category.name")
    List<IncidentEmailDTO> findAllByAssigneeAndCreatedByAfterAndCategoryAndDeletedFalse();

    @Query("SELECT new com.kpmg.cacm.api.dto.response.model.IncidentEmailDTO(count(distinct i.id),i.category.name,i.exception.exceptionDefinitionRun.owner.id) from  Incident  i where i.deleted=false and i.creationTimestamp >= current_date group by i.exception.exceptionDefinitionRun.owner.id,i.category.name")
    List<IncidentEmailDTO> findAllByOwnerAndCreatedByAfterAndCategoryAndDeletedFalse();
}
