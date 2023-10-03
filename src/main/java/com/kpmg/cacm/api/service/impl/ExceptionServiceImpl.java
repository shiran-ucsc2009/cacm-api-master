package com.kpmg.cacm.api.service.impl;

import java.util.List;
import java.util.function.Function;

import javax.persistence.criteria.Predicate;

import com.kpmg.cacm.api.dto.constant.ExceptionStatus;
import com.kpmg.cacm.api.dto.response.model.ExceptionDTO;
import com.kpmg.cacm.api.model.Category;
import com.kpmg.cacm.api.model.Exception;
import com.kpmg.cacm.api.model.ExceptionDefinitionRun;
import com.kpmg.cacm.api.model.User;
import com.kpmg.cacm.api.repository.datatables.ExceptionRepositoryDt;
import com.kpmg.cacm.api.repository.spring.ExceptionRepository;
import com.kpmg.cacm.api.repository.spring.UserRepository;
import com.kpmg.cacm.api.service.ExceptionService;
import com.kpmg.cacm.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class ExceptionServiceImpl implements ExceptionService {

    private final UserRepository userRepository;

    private final ExceptionRepository exceptionRepository;

    private final ExceptionRepositoryDt exceptionRepositoryDt;

    private final UserService userService;

    @Autowired
    public ExceptionServiceImpl(UserRepository userRepository, final ExceptionRepository exceptionRepository, final ExceptionRepositoryDt exceptionRepositoryDt, UserService userService) {
        this.userRepository = userRepository;
        this.exceptionRepository = exceptionRepository;
        this.exceptionRepositoryDt =exceptionRepositoryDt;
        this.userService = userService;
    }

    public List<Exception> findAllByCategoriesAndOwnerAndStatuses(final List<Category> categories, final Long ownerId, final  List<ExceptionStatus> statuses) {
        return this.exceptionRepository.findAllByCategoryInAndOwner_IdAndStatusInAndDeletedFalse(categories, ownerId, statuses);
    }

    @Override
    public Exception save(final Exception exception) {
        return this.exceptionRepository.save(exception);
    }

    @Override
    public Exception findById(final Long id) {
        return this.exceptionRepository.findAllByIdAndDeletedFalse(id);
    }

    @Override
    public DataTablesOutput<ExceptionDTO> findAllByCategoriesAndOwnerDt(
            final List<Category> categories, final Long ownerId, final List<ExceptionStatus> statuses,
            final DataTablesInput dataTablesInput, final Function<Exception, ExceptionDTO> converter) {
        User currentUser = userService.getCurrentUser();
        return this.exceptionRepositoryDt.findAll(
            dataTablesInput,
            null,
            (Specification<Exception>)(root, query, criteriaBuilder)-> {
                Predicate predicate = criteriaBuilder.and(
                    criteriaBuilder.isNotNull(root.<Boolean>get("deleted")),
                    root.<ExceptionDefinitionRun>get("exceptionDefinitionRun").<Category>get("category").in(categories)
                );

                if(ownerId != null) {
                    final User owner = this.userRepository.findByIdAndDeletedFalse(ownerId);
                    predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.equal(root.<ExceptionDefinitionRun>get("exceptionDefinitionRun").<User>get("owner"), owner)
                    );
                }

                if(statuses != null) {
                    predicate = criteriaBuilder.and(
                        predicate,
                        root.<ExceptionStatus>get("status").in(statuses)
                    );
                }
                if (currentUser != null && currentUser.getUserGroup().getName().equals("ASSIGNEE")){
                    predicate = criteriaBuilder.and(predicate,criteriaBuilder.equal(root.<ExceptionDefinitionRun>get("exceptionDefinitionRun").<User>get("defaultAssignee"), currentUser));
                }

                if (currentUser != null && currentUser.getUserGroup().getName().equals("OWNER")){
                    predicate = criteriaBuilder.and(predicate,criteriaBuilder.equal(root.<ExceptionDefinitionRun>get("exceptionDefinitionRun").<User>get("owner"), currentUser));
                }

                return predicate;
            },
            converter
        );
    }
}
