/* PRODUCT : cacm
 * PROJECT : cacm
 * PACKAGE : com.kpmg.cacm.api.controller
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
 * Date : 2/10/2020 - 10:00 AM
 * Since : version 1.0
 * Description :
 * ************* */
package com.kpmg.cacm.api.controller;

import com.kpmg.cacm.api.dto.response.IncidentReasonResponse;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author: ksenanayake1
 * Date : 2/10/2020
 */
@RestController
public interface IncidentReasonController {

    @Secured("FIND_ALL_APPROVE_REASONS")
    @GetMapping("/incident-reason")
    IncidentReasonResponse findAllReasons(
            @RequestParam(value = "category") Long categoryId,
            @RequestParam(value = "action") String approveAction);
}
