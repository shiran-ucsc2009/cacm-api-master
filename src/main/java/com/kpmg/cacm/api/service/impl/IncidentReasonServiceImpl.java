/* PRODUCT : cacm
 * PROJECT : cacm
 * PACKAGE : com.kpmg.cacm.api.service.impl
 * ************************************************************************************************
 *
 * Copyright(C) 2019 KPMG Technology Service All rights reserved.
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF KPMG Technology solution.
 *
 * This copy of the Source Code is intended for KPMG Technology solution's internal use only
 * and is intended for view by persons duly authorized by the management of KPMG Technology solution. No
 * part of this file may be reproduced or distributed in any form or by any
 * means without the written approval of the Management of KPMG Technology solution.
 *
 * *************************************************************************************************
 *
 * REVISIONS:
 * Author : ksenanayake1
 * Date : 2/10/2020 - 10:06 AM
 * Since : version 1.0
 * Description :
 * ************* */
package com.kpmg.cacm.api.service.impl;

import com.kpmg.cacm.api.dto.constant.IncidentApproveAction;
import com.kpmg.cacm.api.model.Category;
import com.kpmg.cacm.api.model.IncidentReasons;
import com.kpmg.cacm.api.repository.spring.CategoryRepository;
import com.kpmg.cacm.api.repository.spring.IncidentReasonsRepository;
import com.kpmg.cacm.api.service.IncidentReasonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Author: ksenanayake1
 * Date : 2/10/2020
 */
@Service
public class IncidentReasonServiceImpl implements IncidentReasonService {

    private final IncidentReasonsRepository incidentReasonsRepository;

    private final CategoryRepository categoryRepository;


    @Autowired
    public IncidentReasonServiceImpl(IncidentReasonsRepository incidentReasonsRepository, CategoryRepository categoryRepository) {
        this.incidentReasonsRepository = incidentReasonsRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<IncidentReasons> findAllReasonsByCategoryAndApproveAction(Long categoryId, String approveAction) {
        return this.incidentReasonsRepository.findAllByApproveActionAndDeletedFalse(
                IncidentApproveAction.valueOf(approveAction.toUpperCase())) ;
    }
}