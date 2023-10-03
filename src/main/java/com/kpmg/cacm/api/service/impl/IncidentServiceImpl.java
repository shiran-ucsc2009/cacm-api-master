package com.kpmg.cacm.api.service.impl;

import java.lang.Exception;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.persistence.criteria.Predicate;

import com.kpmg.cacm.api.dto.constant.AttachmentType;
import com.kpmg.cacm.api.dto.constant.ExceptionStatus;
import com.kpmg.cacm.api.dto.constant.IncidentApproveAction;
import com.kpmg.cacm.api.dto.constant.IncidentFeedbackType;
import com.kpmg.cacm.api.dto.constant.IncidentHistoryRecordType;
import com.kpmg.cacm.api.dto.constant.IncidentStatus;
import com.kpmg.cacm.api.dto.constant.Priority;
import com.kpmg.cacm.api.dto.constant.PrivilegeType;
import com.kpmg.cacm.api.dto.response.model.IncidentAgingByStatusDTO;
import com.kpmg.cacm.api.dto.response.model.IncidentDTO;
import com.kpmg.cacm.api.dto.response.model.IncidentGraphByCategoryDTO;
import com.kpmg.cacm.api.dto.response.model.IncidentGraphByOwnerDTO;
import com.kpmg.cacm.api.dto.response.model.IncidentGraphByPriorityDTO;
import com.kpmg.cacm.api.dto.response.model.RiskMatrixDTO;
import com.kpmg.cacm.api.dto.response.model.RiskMatrixExceptionDTO;
import com.kpmg.cacm.api.dto.response.model.FalsePositiveByActionDTO;
import com.kpmg.cacm.api.dto.response.model.IncidentEmailDTO;
import com.kpmg.cacm.api.dto.constant.*;
import com.kpmg.cacm.api.dto.response.model.*;
import com.kpmg.cacm.api.exceptions.BadRequestException;
import com.kpmg.cacm.api.exceptions.PreConditionFailedException;
import com.kpmg.cacm.api.model.*;
import com.kpmg.cacm.api.repository.datatables.IncidentRepositoryDt;
import com.kpmg.cacm.api.repository.spring.IncidentReasonsRepository;
import com.kpmg.cacm.api.repository.spring.IncidentRepository;
import com.kpmg.cacm.api.service.*;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.Predicate;
import java.lang.Exception;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class IncidentServiceImpl implements IncidentService {

    private final IncidentRepository incidentRepository;

    private final IncidentRepositoryDt incidentRepositoryDt;

    private final UserService userService;

    private final IncidentCommentServiceImpl incidentCommentService;

    private final IncidentHistoryService incidentHistoryService;

    private final IncidentCauseService incidentCauseService;

    private final ExceptionService exceptionService;

    private final FileStorageService fileStorageService;

    private final PrivilegeMappingService privilegeMappingService;

    private final ClientCustomerService clientCustomerService;

    private final IncidentReasonsRepository incidentReasonsRepository;

    @Value("${cacm.config.incident-resolution.attachment-path}")
    private String incidentResolutionsAttachmentPath;

    @Autowired
    public IncidentServiceImpl(
            final IncidentRepository incidentRepository,
            final IncidentRepositoryDt incidentRepositoryDt,
            final UserService userService,
            final IncidentCommentServiceImpl incidentCommentService,
            final IncidentHistoryService incidentHistoryService,
            final IncidentCauseService incidentCauseService,
            final ExceptionService exceptionService,
            final FileStorageService fileStorageService,
            final PrivilegeMappingService privilegeMappingService,
            final ClientCustomerService clientCustomerService,
            final IncidentReasonsRepository incidentReasonsRepository) {
        this.incidentRepository = incidentRepository;
        this.incidentRepositoryDt = incidentRepositoryDt;
        this.userService = userService;
        this.incidentCommentService = incidentCommentService;
        this.incidentHistoryService = incidentHistoryService;
        this.incidentCauseService = incidentCauseService;
        this.exceptionService = exceptionService;
        this.fileStorageService = fileStorageService;
        this.privilegeMappingService = privilegeMappingService;
        this.clientCustomerService = clientCustomerService;
        this.incidentReasonsRepository = incidentReasonsRepository;
    }

    @Override
    @Transactional(rollbackFor = java.lang.Exception.class)
    public void save(final Incident incident) {
        this.incidentHistoryService.addRecord(
            this.incidentRepository.save(incident),
            IncidentHistoryRecordType.CREATE,
            null
        );
    }

    @Override
    public List<Incident> findAll(final List<Category> categories) {
        return this.incidentRepository.findAllByCategoryInAndDeletedFalse(categories);
    }

    @Override
    public List<Incident> findAll(final List<Category> categories, final Long exceptionId, final Long customerId, final List<IncidentStatus> statuses,final String customerRisk) {
        Priority customerRiskLevel;
        User currentUser = userService.getCurrentUser();
        Long assigneeId = null;
        Long ownerId = null;
        if (customerRisk == null || "null".equals(customerRisk) ) {
            customerRiskLevel = null;
        } else {
            customerRiskLevel= Priority.valueOf(customerRisk);
        }
        if (currentUser.getUserGroup().getName().equals("ASSIGNEE")) {
            assigneeId = currentUser.getId();
        } else if (currentUser.getUserGroup().getName().equals("OWNER")) {
            ownerId = currentUser.getId();
        }
        return this.incidentRepository.findAllByCategoryInAndException_IdAndClientCustomer_IdAndStatusInAndDeletedFalse(categories, exceptionId, customerId, statuses,customerRiskLevel, assigneeId, ownerId);
    }

    @Override
    public Incident findById(final Long id) {
        return this.incidentRepository.findByIdAndDeletedFalse(id);
    }

    @Override
    @Transactional(rollbackFor = java.lang.Exception.class)
    public void batchAssign(final List<Long> incidentIds, final Long userId, final Date dueDate, final String comment) {
        final User assignee = this.findAndValidateAssignee(userId);
        if(dueDate.compareTo(new Date())==0) {
            if (dueDate.before(new Date())) {
                throw new PreConditionFailedException("Due date cannot be a past date");
            }
        }
        final Map<String, String> historyParam = new HashMap<>(16);
        historyParam.put("assignee", assignee.getName());
        historyParam.put("comment", comment);
        historyParam.put("dueDate", new SimpleDateFormat("yyyy-MM-dd").format(dueDate));

        incidentIds.forEach(incidentId -> {
            final Incident incident = this.findAndValidateIncident(incidentId);
            if (incident.getStatus() != IncidentStatus.OPEN &&
                incident.getStatus() != IncidentStatus.ASSIGNED) {
                throw new PreConditionFailedException("Incident status should be OPEN or ASSIGNED");
            }
            incident.setAssignee(assignee);
            incident.setDueDate(dueDate);
            incident.setStatus(IncidentStatus.ASSIGNED);
            this.incidentRepository.save(incident);

            if(comment != null) {
                this.addIncidentComment(comment, incident);
            }
            this.incidentHistoryService.addRecord(incident, IncidentHistoryRecordType.ASSIGN, historyParam);
        });
    }

    @Override
    @Transactional(rollbackFor = java.lang.Exception.class)
    public void resolve(final MultipartFile file, final Long incidentId, final Long incidentCauseId, final String comment) {
        final IncidentCause cause = this.findAndValidateIncidentCause(incidentCauseId);
        final IncidentResolution resolution = new IncidentResolution();
        resolution.setIncidentId(incidentId);
        resolution.setIncidentCause(cause);

        final Incident incident = this.findAndValidateIncident(incidentId);
        if (incident.getStatus() != IncidentStatus.ASSIGNED) {
            throw new PreConditionFailedException("Cannot resolve " + incident.getStatus().name() + " incidents");
        }
        if (this.userService.getCurrentUser().getId() != incident.getAssignee().getId()) {
            throw new PreConditionFailedException("Not Authorized to resolve this incident");
        }
        incident.setIncidentResolution(resolution);
        incident.setStatus(IncidentStatus.RESOLVED);
        this.incidentRepository.save(incident);

        this.addIncidentComment(comment, incident);

        final Map<String, String> historyParam = new HashMap<>(16);
        historyParam.put("cause", cause.getName());
        historyParam.put("comment", comment);

        if (file != null && !file.isEmpty()) {
            final String fileNameWithExt = this.fileStorageService.saveFile(
                    file,
                    incident.getIncidentResolution().getId() + "-" + FilenameUtils.removeExtension(file.getOriginalFilename()),
                    this.incidentResolutionsAttachmentPath
            );
            if (fileNameWithExt != null) {
                final Attachment attachment = new Attachment();
                attachment.setName(fileNameWithExt);
                attachment.setAttachmentType(AttachmentType.INCIDENT_RESOLUTION);
                attachment.setEntityId(incident.getIncidentResolution().getId());
                incident.getIncidentResolution().setAttachment(attachment);
                historyParam.put("file" , fileNameWithExt);
            }
        }
        this.incidentRepository.save(incident);
        this.incidentHistoryService.addRecord(incident, IncidentHistoryRecordType.RESOLVE, historyParam);
    }



    @Override
    @Transactional(rollbackFor = java.lang.Exception.class)
    public void rejectResolution(final Long incidentId, final Long userId, final String comment, final Date dueDate) {
        final IncidentFeedback feedback = new IncidentFeedback();
        feedback.setFeedbackType(IncidentFeedbackType.RESOLUTION_REJECTED);

        final User assignee = this.findAndValidateAssignee(userId);
        final Incident incident = this.findAndValidateIncident(incidentId);
        if (incident.getStatus() != IncidentStatus.RESOLVED) {
            throw new PreConditionFailedException("Cannot reject resolution of " + incident.getStatus().name() + " incidents");
        }

        incident.setAssignee(assignee);
        incident.setStatus(IncidentStatus.ASSIGNED);
        incident.getIncidentResolution().setIncidentFeedback(feedback);

        if (dueDate != null || (dueDate.compareTo(new Date())==0) ) {
            if (dueDate.before(new Date())) {
                throw new PreConditionFailedException("Due date cannot be a past date");
            }
            incident.setDueDate(dueDate);
        }

        this.incidentRepository.save(incident);

        this.addIncidentComment(comment, incident);

        final Map<String, String> historyParam = new HashMap<>(16);
        historyParam.put("assignee", assignee.getName());
        historyParam.put("comment", comment);
        historyParam.put("dueDate", new SimpleDateFormat("yyyy-MM-dd").format(dueDate));
        this.incidentHistoryService.addRecord(incident, IncidentHistoryRecordType.REJECT_RESOLUTION, historyParam);
    }

    @Override
    @Transactional(rollbackFor = java.lang.Exception.class)
    public void approveResolution(final Long incidentId, final IncidentApproveAction approveAction, final String comment, final Long incidentApproveReason) {
        final Incident incident = this.findAndValidateIncident(incidentId);
        if (incident.getStatus() != IncidentStatus.RESOLVED) {
            throw new PreConditionFailedException("Cannot approve resolution of " + incident.getStatus().name() + " incidents");
        }
        this.completeApproval(approveAction, comment, incident, IncidentHistoryRecordType.APPROVE_RESOLUTION,incidentApproveReason);
    }

    @Override
    public void batchApprove(List<Long> incidentIds, IncidentApproveAction approveAction, String comment,final Long incidentApproveReason) {

        incidentIds.forEach(incidentId -> {
            final Incident incident = this.findAndValidateIncident(incidentId);
            if (incident.getStatus() != IncidentStatus.RESOLVED) {
                throw new PreConditionFailedException("Cannot approve resolution of " + incident.getStatus().name() + " incidents");
            }
            this.completeApproval(approveAction, comment, incident, IncidentHistoryRecordType.APPROVE_RESOLUTION,incidentApproveReason);
        });


    }

    @Override
    public void batchFinalize(List<Long> incidentIds, String comment) {
        incidentIds.forEach(incidentId -> {

            final Incident incident = this.findAndValidateIncident(incidentId);
            if (incident.getStatus() != IncidentStatus.APPROVED) {
                throw new PreConditionFailedException("Only APPROVED incidents are eligible for FINALIZE");
            }
            incident.setStatus(IncidentStatus.FINALIZED);
            incident.getIncidentResolution().getIncidentFeedback().setFinalized(true);
            this.incidentRepository.save(incident);

            this.addIncidentComment(comment, incident);

            final Map<String, String> historyParam = new HashMap<>(16);
            historyParam.put("comment", comment);
            this.incidentHistoryService.addRecord(incident, IncidentHistoryRecordType.FINALIZE_APPROVAL, historyParam);

            if(this.incidentRepository.findAllByExceptionAndDeletedFalse(incident.getException())
                    .stream()
                    .allMatch(incidentObj -> incidentObj.getStatus() == IncidentStatus.FINALIZED)) {
                incident.getException().setStatus(ExceptionStatus.COMPLETED);
                this.exceptionService.save(incident.getException());
            }

        });

    }

    @Override
    @Transactional(rollbackFor = java.lang.Exception.class)
    public void overrideApproval(final Long incidentId, final IncidentApproveAction approveAction, final String comment, final Long incidentApproveReason) {
        final Incident incident = this.findAndValidateIncident(incidentId);
        if (incident.getStatus() != IncidentStatus.APPROVED) {
            throw new PreConditionFailedException("Only APPROVED incidents are eligible for OVERRIDE_APPROVAL");
        }
        this.completeApproval(approveAction, comment, incident, IncidentHistoryRecordType.OVERRIDE_APPROVAL,incidentApproveReason);
    }

    private void completeApproval(
            final IncidentApproveAction approveAction,
            final String comment,
            final Incident incident,
            final IncidentHistoryRecordType incidentHistoryRecordType,
            final Long incidentApproveReason) {

        IncidentReasons incidentReasons=this.incidentReasonsRepository.findByIdAndDeletedFalse(incidentApproveReason);
        final IncidentFeedback feedback = new IncidentFeedback();
        if (incidentReasons != null) {
            feedback.setIncidentReasons(incidentReasons);
        }
        feedback.setFeedbackType(IncidentFeedbackType.RESOLUTION_APPROVED);
        feedback.setApproveAction(approveAction);
        feedback.setFinalized(false);
        incident.setStatus(IncidentStatus.APPROVED);
        incident.getIncidentResolution().setIncidentFeedback(feedback);
        this.incidentRepository.save(incident);

        this.addIncidentComment(comment, incident);

        final Map<String, String> historyParam = new HashMap<>(16);
        historyParam.put("action", approveAction.name());
        historyParam.put("comment", comment);
        if (incidentReasons != null) {
            historyParam.put("reason", incidentReasons.getReason());
        }
        this.incidentHistoryService.addRecord(incident, incidentHistoryRecordType, historyParam);
    }

    @Override
    @Transactional(rollbackFor = java.lang.Exception.class)
    public void finalizeApproval(final Long incidentId, final String comment) {
        final Incident incident = this.findAndValidateIncident(incidentId);
        if (incident.getStatus() != IncidentStatus.APPROVED) {
            throw new PreConditionFailedException("Only APPROVED incidents are eligible for FINALIZE");
        }
        incident.setStatus(IncidentStatus.FINALIZED);
        incident.getIncidentResolution().getIncidentFeedback().setFinalized(true);
        this.incidentRepository.save(incident);

        this.addIncidentComment(comment, incident);

        final Map<String, String> historyParam = new HashMap<>(16);
        historyParam.put("comment", comment);
        this.incidentHistoryService.addRecord(incident, IncidentHistoryRecordType.FINALIZE_APPROVAL, historyParam);

        if(this.incidentRepository.findAllByExceptionAndDeletedFalse(incident.getException())
            .stream()
            .allMatch(incidentObj -> incidentObj.getStatus() == IncidentStatus.FINALIZED)) {
            incident.getException().setStatus(ExceptionStatus.COMPLETED);
            this.exceptionService.save(incident.getException());
        }
    }

    @Override
    @Transactional(rollbackFor = java.lang.Exception.class)
    public void rejectApproval(final Long incidentId, final String comment, final Date dueDate) {

        final Incident incident = this.findAndValidateIncident(incidentId);
        final User assignee = incident.getException().getExceptionDefinitionRun().getOwner();

        if (incident.getStatus() != IncidentStatus.APPROVED) {
            throw new PreConditionFailedException("Only APPROVED incidents are eligible for REJECT_APPROVAL");
        }
        incident.setStatus(IncidentStatus.RESOLVED);
        incident.getIncidentResolution().setIncidentFeedback(null);
        this.incidentRepository.save(incident);

        this.addIncidentComment(comment, incident);

        final Map<String, String> historyParam = new HashMap<>(16);
        historyParam.put("assignee", assignee.getName());
        historyParam.put("comment", comment);
        historyParam.put("dueDate", new SimpleDateFormat("yyyy-MM-dd").format(dueDate));
        this.incidentHistoryService.addRecord(incident, IncidentHistoryRecordType.REJECT_APPROVAL, historyParam);
    }

    @Override
    @Transactional(rollbackFor = java.lang.Exception.class)
    public void setDueDate(final Long incidentId, final Date dueDate) {
        final Incident incident = this.findAndValidateIncident(incidentId);
        if (incident.getStatus() == IncidentStatus.FINALIZED) {
            throw new PreConditionFailedException("Cannot change due date of completed incidents");
        }
        if (dueDate.compareTo(new Date())==0) {
            if (dueDate.before(new Date())) {
                throw new PreConditionFailedException("Due date cannot be a past date");
            }
        }
        incident.setDueDate(dueDate);
        this.incidentRepository.save(incident);

        final Map<String, String> historyParam = new HashMap<>(16);
        historyParam.put("dueDate", new SimpleDateFormat("yyyy-MM-dd").format(dueDate));
        this.incidentHistoryService.addRecord(incident, IncidentHistoryRecordType.APPROVE_RESOLUTION, historyParam);
    }

    @Override
    public long countAllByCategoryAndStatusAndClientCustomer(final List<Category> categories, final IncidentStatus incidentStatus, final String clientCustomerId) {
        User currentUser = userService.getCurrentUser();
        Long assigneeId = null;
        Long ownerId = null;
        if (currentUser.getUserGroup().getName().equals("ASSIGNEE")) {
            assigneeId = currentUser.getId();
        } else if (currentUser.getUserGroup().getName().equals("OWNER")) {
            ownerId = currentUser.getId();
        }
        return this.incidentRepository.countAllByCategoryInAndStatusAndDeletedFalse(categories, incidentStatus, clientCustomerId, assigneeId, ownerId);
    }

    private void addIncidentComment(final String comment, final Incident incident) {
        if (comment != null && !comment.trim().isEmpty()) {
            final IncidentComment incidentComment = new IncidentComment();
            incidentComment.setIncident(incident);
            incidentComment.setComment(comment);
            this.incidentCommentService.save(incidentComment);
        }
    }

    private Incident findAndValidateIncident(final Long incidentId) {
        final Incident incident = this.findById(incidentId);
        if (incident == null) {
            throw new BadRequestException("Invalid incident ID");
        }
        return incident;
    }

    private IncidentCause findAndValidateIncidentCause(final Long incidentCauseId) {
        final IncidentCause cause = this.incidentCauseService.findById(incidentCauseId);
        if (cause == null) {
            throw new BadRequestException("Invalid incident cause ID");
        }
        return cause;
    }

    private User findAndValidateAssignee(final Long userId) {
        final User assignee = this.userService.findById(userId);
        final List<String> privileges = this.privilegeMappingService
            .findAllByUserGroupAndPrivilegeType(assignee.getUserGroup(), PrivilegeType.API)
            .stream()
            .map(privilegeMapping -> privilegeMapping.getPrivilege().getName())
            .collect(Collectors.toList());
        if (!privileges.contains("RESOLVE_INCIDENT")) {
            throw new PreConditionFailedException("User " + assignee.getUsername() + " (" + assignee.getName() + ") does not have permission to RESOLVE incidents");
        }
        return assignee;
    }

    @Override
    public IncidentAgingByStatusDTO getIncidentAgingDetails() {

        final Collection<IncidentStatus> status = Arrays.asList(
            IncidentStatus.OPEN,
            IncidentStatus.ASSIGNED,
            IncidentStatus.RESOLVED,
            IncidentStatus.APPROVED
        );

        // check assignee or owner for retrieve data
        User currentUser = userService.getCurrentUser();
        Long assigneeId = null;
        Long ownerId = null;
        if (currentUser.getUserGroup().getName().equals("ASSIGNEE")) {
            assigneeId = currentUser.getId();
        } else if (currentUser.getUserGroup().getName().equals("OWNER")) {
            ownerId = currentUser.getId();
        }

        final Map<IncidentStatus, Map<Integer, Integer>> countByStatus = new HashMap<>();
        for (final IncidentStatus incidentStatus : status) {
            countByStatus.put(incidentStatus, this.getIncidentsByStatus(
                this.incidentRepository.findAllByStatusAndCategoryInAndDeletedFalse(
                    incidentStatus,
                    this.userService.getCurrentUser().getCategories(),
                        assigneeId,
                        ownerId
                )
            ));
        }

        final IncidentAgingByStatusDTO agingDTO = new IncidentAgingByStatusDTO();
        agingDTO.setCountByStatus(countByStatus);
        return agingDTO;
    }

    @Override
    public List<Incident> findAllIncidentByAge(final Integer ageOfIncident,final List<IncidentStatus> statuses) {
        return this.filterIncidentsByAge(
            this.incidentRepository.findAllByStatusInAndCategoryInAndDeletedFalse(
                statuses,
                this.userService.getCurrentUser().getCategories()
            ),
            ageOfIncident
        );
    }

    @Override
    public List<Incident> findAllIncidentsByDateRange(final IncidentStatus incidentStatus, final List<Category> categories, final Date fromDate, final Date toDate) {
        return this.incidentRepository.findAllByStatusAndCategoryInAndCreationTimestampBetweenAndDeletedFalse(incidentStatus,categories,fromDate,toDate);
    }

    @Override
    public IncidentGraphByCategoryDTO getIncidentByCategory() {
        final Collection<IncidentStatus> incidentStatuses=Arrays.asList(
                IncidentStatus.OPEN,
                IncidentStatus.ASSIGNED,
                IncidentStatus.RESOLVED,
                IncidentStatus.APPROVED
        );

        final Map<String, Map<IncidentStatus, Long>> countByCategory = new HashMap<>();
        for (final Category category : this.userService.getCurrentUser().getCategories()) {
            final Map<IncidentStatus, Long> incidentCountByStatus = new HashMap<>();
            for (final IncidentStatus incidentStatus : incidentStatuses) {
                incidentCountByStatus.put(
                        incidentStatus,
                    this.incidentRepository.countByStatusAndCategoryAndDeletedFalse(
                            incidentStatus,
                            category
                    )
                );
            }
            countByCategory.put(String.valueOf(category.getName()), incidentCountByStatus);
        }

        final IncidentGraphByCategoryDTO graphByCategoryDTO = new IncidentGraphByCategoryDTO();
        graphByCategoryDTO.setCountByCatgory(countByCategory);
        return  graphByCategoryDTO;
    }

    @Override
    public List<FalsePositiveByActionDTO> getIncidentByAction() {

        final Collection<IncidentStatus> status = Arrays.asList(
                IncidentStatus.FINALIZED
                );

        final Collection<IncidentApproveAction> incidentApproveActions = Arrays.asList(
                IncidentApproveAction.DISCOUNT,
                IncidentApproveAction.REPORT
        );
        List<FalsePositiveByActionDTO> falsePositiveByActionDTOS=new ArrayList<>();
        for (final IncidentApproveAction incidentApproveAction : incidentApproveActions) {
            final FalsePositiveByActionDTO falsePositiveByActionDTO=new FalsePositiveByActionDTO();
            long incidentCount=0;

            final List countCategory=new ArrayList();

            for (final Category category : this.userService.getCurrentUser().getCategories()) {
                final List list=new ArrayList();
                final Map<String, Long> incidentCountByCategory = new HashMap<>();
                incidentCount+=this.incidentRepository.countByStatusAndIncidentResolution_IncidentFeedback_ApproveActionAndCategoryAndDeletedFalse(status,incidentApproveAction, category);
               list.add(String.valueOf(category.getName()));
               list.add(this.incidentRepository.countByStatusAndIncidentResolution_IncidentFeedback_ApproveActionAndCategoryAndDeletedFalse(status,incidentApproveAction, category)
               );
                incidentCountByCategory.put(
                        String.valueOf(category.getName()),
                        this.incidentRepository.countByStatusAndIncidentResolution_IncidentFeedback_ApproveActionAndCategoryAndDeletedFalse(status,incidentApproveAction, category)
                );

                countCategory.add(list);
            }
            falsePositiveByActionDTO.setName(String.valueOf(incidentApproveAction));
            falsePositiveByActionDTO.setY(incidentCount);
            falsePositiveByActionDTO.setDrilldown(String.valueOf(incidentApproveAction));
            falsePositiveByActionDTO.setCountbyCategoryAction(countCategory);
            falsePositiveByActionDTOS.add(falsePositiveByActionDTO);
        }

        return falsePositiveByActionDTOS;

    }

    @Override
    public IncidentGraphByPriorityDTO getIncidentByPriority() {

        List<Priority> priorities = Arrays.asList(Priority.HIGH, Priority.MEDIUM, Priority.LOW);
        List<Category> categories = this.userService.getCurrentUser().getCategories();

        final Map<Priority, Map<String, Long>> countByPriority = new HashMap<>();
        for (final Priority priority : priorities){
            final Map<String, Long> incidentCountByCategory = new HashMap<>();
            for (final Category category: categories){
                final long incidentCount= this.incidentRepository.countByCategoryAndException_ExceptionDefinitionRun_PriorityAndDeletedFalse(category,priority);
                incidentCountByCategory.put(String.valueOf(category.getName()), incidentCount);
            }
            countByPriority.put(priority, incidentCountByCategory);
        }

        final IncidentGraphByPriorityDTO incidentGraphByPriorityDTO = new IncidentGraphByPriorityDTO();
        incidentGraphByPriorityDTO.setCountByPriority(countByPriority);
        return incidentGraphByPriorityDTO;
    }

    @Override
    public List<IncidentGraphByOwnerDTO> getIncidentCountByOwner(List<Category> categories) {
        return this.incidentRepository.findIncidentGraphByOwnerByCategory(categories);
    }

    @Override
    public List<RiskMatrixDTO> riskMatrixCount(List<Category> categories) {
        final List<IncidentStatus> status = Arrays.asList(
                IncidentStatus.OPEN,
                IncidentStatus.ASSIGNED,
                IncidentStatus.RESOLVED,
                IncidentStatus.APPROVED
        );
        return this.incidentRepository.findRiskMatrixDTOByPriority(categories,status);
    }

    @Override
    public List<RiskMatrixExceptionDTO> riskMatrixExceptionList(List<Category> categories, Priority customerRisk, Priority scenarioRisk) {
        return this.incidentRepository.findRiskMatrixExceptionDTOByPriority(categories,customerRisk,scenarioRisk);
    }

    @Override
    public DataTablesOutput<IncidentDTO> findAllByCategoriesAndStatusDt(
            List<Category> categories, List<IncidentStatus> statuses,
            Long clientCustomerId, Long exceptionId,
            Priority customerRisk,Priority scenarioRisk,
            DataTablesInput dataTablesInput, Function<Incident, IncidentDTO> converter) {

        User currentUser = userService.getCurrentUser();
        return this.incidentRepositoryDt.findAll(
            dataTablesInput,
            null,
            (Specification<Incident>)(root, query, criteriaBuilder) -> {
                Predicate predicate = criteriaBuilder.isFalse(root.get("deleted"));
                if(categories != null && !categories.isEmpty()){
                    predicate = criteriaBuilder.and(predicate, root.<Category>get("category").in(categories));
                }
                if (statuses != null && !statuses.isEmpty()){
                    predicate = criteriaBuilder.and(predicate, root.<IncidentStatus>get("status").in(statuses));
                }
                if (clientCustomerId != null){
                    predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(
                        root.<ClientCustomer>get("clientCustomer"),
                        this.clientCustomerService.findById(clientCustomerId)
                    ));
                }
                if (exceptionId != null){
                    predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(
                        root.<Exception>get("exception"),
                        this.exceptionService.findById(exceptionId)
                    ));
                }
                if (scenarioRisk != null){
                    predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(
                            root.<Exception>get("exception").<ExceptionDefinitionRun>get("exceptionDefinitionRun").<Priority>get("priority"),
                            scenarioRisk
                    ));
                }
                if (customerRisk != null){
                    predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(
                            root.<ClientCustomer>get("clientCustomer").<Priority>get("priority"),
                            customerRisk
                    ));
                }
                if (currentUser != null && currentUser.getUserGroup().getName().equals("ASSIGNEE")){
                    predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(
                            root.<ClientCustomer>get("assignee"), currentUser));
                }

                if (currentUser != null && currentUser.getUserGroup().getName().equals("OWNER")){
                    predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(
                            root.<Exception>get("exception").<ExceptionDefinitionRun>get("exceptionDefinitionRun").<Priority>get("owner"), currentUser));
                }

                return predicate;
            },
            converter
        );
    }

    @Override
    public List<IncidentEmailDTO> findAllByCategoryAndAssigneeAndCreationDate() {
        return this.incidentRepository.findAllByAssigneeAndCreatedByAfterAndCategoryAndDeletedFalse();
    }

    @Override
    public List<IncidentEmailDTO> findAllByCategoryAndOwnerAndCreationDate() {
        return this.incidentRepository.findAllByOwnerAndCreatedByAfterAndCategoryAndDeletedFalse();
    }

    private List<Incident> filterIncidentsByAge(final List<Incident> incidents, final int targetAgeOfIncident) {
        final List<Incident> incidentList = new ArrayList<>();
        for (final Incident incident : incidents) {
            final int ageOfIncident = IncidentServiceImpl.getAgeOfIncident(incident.getCreationTimestamp());
            if (targetAgeOfIncident == ageOfIncident || targetAgeOfIncident == 6 && ageOfIncident > targetAgeOfIncident) {
                incidentList.add(this.incidentRepository.findIncidentByIdAndDeletedFalse(incident.getId()));
            }
        }
        return incidentList;
    }

    private Map<Integer, Integer> getIncidentsByStatus(final List<Incident> incidents) {

        final Map<Integer, Integer> incidentCountByAging = new HashMap<Integer, Integer>(){{
            this.put(0, 0);
            this.put(1, 0);
            this.put(2, 0);
            this.put(3, 0);
            this.put(4, 0);
            this.put(5, 0);
            this.put(6, 0);
        }};
        for (final Incident incidentIds : incidents) {
            int ageOfIncident = IncidentServiceImpl.getAgeOfIncident(incidentIds.getCreationTimestamp());
            if (ageOfIncident > 5) {
                ageOfIncident = 6;
            }
            incidentCountByAging.put(ageOfIncident, incidentCountByAging.get(ageOfIncident)+1);
        }
        return incidentCountByAging;
    }

    private static Integer getAgeOfIncident(final Date incidentCreationDate){
        long diffDays =(new Date().getTime() - incidentCreationDate.getTime())/ (24 * 60 * 60 * 1000);
        return  Math.abs((int)diffDays);
    }
}
