/* PRODUCT : CACM
 * PROJECT : cacm-api
 * PACKAGE : com.kpmg.cacm.api.analytics.controller
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
 * Author : kperera5
 * Date : 11/18/2019 - 10:26 AM
 * Since : version 1.0
 * Description :
 * ************* */
package com.kpmg.cacm.api.analytics.controller;

import com.kpmg.cacm.api.dto.response.RunQueryResponse;

import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * Author: kperera5
 * Date : 11/18/2019
 */
@RestController
public interface ClientAnalyticsController {

    @Secured("FIND_ALL_STG_CUSTOMERS")
    @GetMapping("/stg-customers-list/{firstname}")
    RunQueryResponse findAllCustomerByName(@PathVariable(value = "firstname") String firstName);

    @Secured("FIND_ALL_STG_CUSTOMERS")
    @GetMapping("/stg-customers1/{nic}")
    RunQueryResponse findAllCustomerByNic(@PathVariable(value = "nic") String nic);

    @Secured("FIND_ALL_STG_ACCOUNTS_BY_ID")
    @GetMapping("/stg-accounts/{id}")
    RunQueryResponse findAllAccountById(@PathVariable(value = "id") Long id);

    @Secured("FIND_ALL_STG_ACCOUNTS_BY_ID")
    @GetMapping("/stg-selected-accounts-/{accounts}")
    RunQueryResponse selectedAccountById(@PathVariable(value = "accounts") List<String> accounts);


    @Secured("FIND_STG_CUSTOMER_BY_ID")
    @GetMapping("/stg-customers/{id}")
    RunQueryResponse findCustomerById(@PathVariable(value = "id") Long id);

    @Secured("FIND_ALL_STG_TRANS_BY_DATE_RANGE")
    @GetMapping("/stg-transactions1/{customer}/{accounts}/{fromDate}/{toDate}")
    RunQueryResponse findAllTransactions(
                 @PathVariable(value = "customer") Long customerId,
                 @PathVariable(value = "accounts") List<String> accounts,
                 @PathVariable(value = "fromDate") String fromDate,
                 @PathVariable(value = "toDate") String toDate
    ) throws IOException;

    @Secured("IS_LINK_ANALYSIS_ENABLED")
    @GetMapping("/link-analysis/link-analysis-enable")
    Boolean isLinkAnalysisEnable();

}
