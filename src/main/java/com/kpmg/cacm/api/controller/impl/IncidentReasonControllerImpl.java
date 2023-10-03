/* PRODUCT : cacm
 * PROJECT : cacm
 * PACKAGE : com.kpmg.cacm.api.controller.impl
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
 * Date : 2/10/2020 - 10:01 AM
 * Since : version 1.0
 * Description :
 * ************* */
package com.kpmg.cacm.api.controller.impl;

import com.kpmg.cacm.api.controller.IncidentReasonController;
import com.kpmg.cacm.api.dto.response.IncidentReasonResponse;
import com.kpmg.cacm.api.dto.response.model.IncidentReasonDTO;
import com.kpmg.cacm.api.model.IncidentReasons;
import com.kpmg.cacm.api.service.IncidentReasonService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Author: ksenanayake1
 * Date : 2/10/2020
 */

@Component
public class IncidentReasonControllerImpl implements IncidentReasonController {

    private final IncidentReasonService incidentReasonService;

    private final ModelMapper modelMapper;


    public IncidentReasonControllerImpl(
            final IncidentReasonService incidentReasonService,
            final ModelMapper modelMapper) {
        this.incidentReasonService = incidentReasonService;
        this.modelMapper = modelMapper;
    }

    @Override
    public IncidentReasonResponse findAllReasons(final Long categoryId, final String approveAction) {
        final List<IncidentReasonDTO> incidentReasons=
               this.incidentReasonService.findAllReasonsByCategoryAndApproveAction(categoryId,approveAction)
                        .stream()
                        .map(incidentReasons1 -> this.modelMapper.map(incidentReasons1, IncidentReasonDTO.class))
                        .collect(Collectors.toList());
        return IncidentReasonResponse.builder().incidentReasonDTOS(incidentReasons).build();
    }
}