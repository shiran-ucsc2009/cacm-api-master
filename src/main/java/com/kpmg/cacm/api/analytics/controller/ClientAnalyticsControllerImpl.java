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
 * Date : 11/18/2019 - 11:24 AM
 * Since : version 1.0
 * Description :
 * ************* */
package com.kpmg.cacm.api.analytics.controller;

import com.kpmg.cacm.api.analytics.service.ClientAnalyticsService;
import com.kpmg.cacm.api.dto.response.RunQueryResponse;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * Author: kperera5
 * Date : 11/18/2019
 */
@Component
public class ClientAnalyticsControllerImpl implements ClientAnalyticsController {

    private final ClientAnalyticsService clientAnalyticsService;

    public ClientAnalyticsControllerImpl(ClientAnalyticsService clientAnalyticsService) {
        this.clientAnalyticsService = clientAnalyticsService;
    }

    @Override
    public RunQueryResponse findAllCustomerByName(String firstname) {
        return RunQueryResponse.builder()
                .resultList(this.clientAnalyticsService.findAllCustomerByFirstName(firstname))
                .build();
    }

    @Override
    public RunQueryResponse findAllCustomerByNic(String nic) {
        return RunQueryResponse.builder()
                .resultList(this.clientAnalyticsService.findAllCustomerByNic(nic))
                .build();
    }

    @Override
    public RunQueryResponse findAllAccountById(Long id) {
        return RunQueryResponse.builder()
                .resultList(this.clientAnalyticsService.findAllAccountsById(id))
                .build();
    }

    @Override
    public RunQueryResponse selectedAccountById(List<String> accounts) {
        return RunQueryResponse.builder()
                .resultList(this.clientAnalyticsService.selectedAccountById(accounts))
                .build();
    }

    @Override
    public RunQueryResponse findCustomerById(Long id) {
        return RunQueryResponse.builder()
                .resultList(this.clientAnalyticsService.findAllAccountsById(id))
                .build();
    }

    @Override
    public RunQueryResponse findAllTransactions(Long customerId, List<String> accounts, String fromDate, String toDate) {
        return RunQueryResponse.builder()
                .resultList(this.clientAnalyticsService.findAllTransactions(customerId,accounts,fromDate,toDate))
                .build();
    }

    @Override
    public Boolean isLinkAnalysisEnable() {
        return this.clientAnalyticsService.isLinkAnalysisEnable();
    }

}
