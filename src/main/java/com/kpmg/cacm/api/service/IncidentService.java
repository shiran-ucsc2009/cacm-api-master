package com.kpmg.cacm.api.service;

import java.util.Date;
import java.util.List;
import java.util.function.Function;

import com.kpmg.cacm.api.dto.constant.IncidentApproveAction;
import com.kpmg.cacm.api.dto.constant.IncidentStatus;
import com.kpmg.cacm.api.dto.constant.Priority;
import com.kpmg.cacm.api.dto.response.model.IncidentAgingByStatusDTO;
import com.kpmg.cacm.api.dto.response.model.IncidentDTO;
import com.kpmg.cacm.api.dto.response.model.IncidentGraphByCategoryDTO;
import com.kpmg.cacm.api.dto.response.model.IncidentGraphByOwnerDTO;
import com.kpmg.cacm.api.dto.response.model.IncidentGraphByPriorityDTO;
import com.kpmg.cacm.api.dto.response.model.RiskMatrixDTO;
import com.kpmg.cacm.api.dto.response.model.RiskMatrixExceptionDTO;
import com.kpmg.cacm.api.dto.response.model.FalsePositiveByActionDTO;
import com.kpmg.cacm.api.dto.response.model.IncidentEmailDTO;
import com.kpmg.cacm.api.model.Category;
import com.kpmg.cacm.api.model.Incident;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.web.multipart.MultipartFile;

public interface IncidentService {

    void save(Incident incident);

    List<Incident> findAll(List<Category> categories);

    List<Incident> findAll(List<Category> categories, Long exceptionId, Long customerId, List<IncidentStatus> statuses,String priority);

    Incident findById(Long id);

    void batchAssign(List<Long> incidentIds, Long userId, Date dueDate, String comment);

    void resolve(MultipartFile file, Long incidentId, Long incidentCauseId, String comment);

    void rejectResolution(Long incidentId, Long userId, String comment, Date dueDate);

    void approveResolution(Long incidentId, IncidentApproveAction approveAction, String comment, final Long incidentApproveReason);

    void batchApprove(List<Long> incidentIds,IncidentApproveAction approveAction, String comment, final Long incidentApproveReason);

    void batchFinalize(List<Long> incidentIds,String comment);

    void overrideApproval(Long incidentId, IncidentApproveAction approveAction, String comment, final Long incidentApproveReason);

    void finalizeApproval(Long incidentId, String comment);

    void rejectApproval(Long incidentId, String comment, Date dueDate);

    void setDueDate(Long incidentId, Date dueDate);

    long countAllByCategoryAndStatusAndClientCustomer(List<Category> categories, IncidentStatus incidentStatus, String clientCustomerId);

    IncidentAgingByStatusDTO getIncidentAgingDetails();

    List<Incident> findAllIncidentByAge(Integer dateDiff,List<IncidentStatus> statuses);

    List<Incident> findAllIncidentsByDateRange(IncidentStatus incidentStatus,List<Category> categories, Date fromDate,Date toDate);

    IncidentGraphByCategoryDTO getIncidentByCategory();

    List<FalsePositiveByActionDTO> getIncidentByAction();

    IncidentGraphByPriorityDTO getIncidentByPriority();

    List<IncidentGraphByOwnerDTO> getIncidentCountByOwner(List<Category> categories);

    List<RiskMatrixDTO> riskMatrixCount(List<Category> categories);

    List<RiskMatrixExceptionDTO> riskMatrixExceptionList(List<Category> categories, Priority customerRisk, Priority  scenarioRisk);

    DataTablesOutput<IncidentDTO> findAllByCategoriesAndStatusDt(List<Category> categories, List<IncidentStatus> statuses, Long clientCustomerId, Long exceptionId,Priority customerRisk,Priority scenarioRisk, DataTablesInput dataTablesInput, Function<Incident, IncidentDTO> converter);

    List<IncidentEmailDTO> findAllByCategoryAndAssigneeAndCreationDate();

    List<IncidentEmailDTO> findAllByCategoryAndOwnerAndCreationDate();

}