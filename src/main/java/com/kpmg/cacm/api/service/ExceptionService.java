package com.kpmg.cacm.api.service;

import java.util.List;
import java.util.function.Function;

import com.kpmg.cacm.api.dto.constant.ExceptionStatus;
import com.kpmg.cacm.api.dto.response.model.ExceptionDTO;
import com.kpmg.cacm.api.model.Category;
import com.kpmg.cacm.api.model.Exception;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

public interface ExceptionService {

    List<Exception> findAllByCategoriesAndOwnerAndStatuses(List<Category> categories, Long ownerId, List<ExceptionStatus> statuses);

    Exception save(Exception exception);

    Exception findById(Long id);

    DataTablesOutput<ExceptionDTO> findAllByCategoriesAndOwnerDt(List<Category> categories, Long ownerId, List<ExceptionStatus> statuses, DataTablesInput dataTablesInput, Function<Exception, ExceptionDTO> converter);

}
