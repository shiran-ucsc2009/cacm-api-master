/* PRODUCT : CACM
 * PROJECT : cacm-api
 * PACKAGE : com.kpmg.cacm.api.analytics.service
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
 * Date : 11/18/2019 - 3:43 PM
 * Since : version 1.0
 * Description :
 * ************* */
package com.kpmg.cacm.api.analytics.service;

import java.util.List;
import java.util.Map;

/**
 * Author: kperera5
 * Date : 11/18/2019
 */
public interface ClientAnalyticsService {

    List<Map<String, Object>> findAll();

    List<Map<String,Object>> findAllCustomerByNic(String nic);

    List<Map<String,Object>> findAllCustomerByFirstName(String firstName);

    List<Map<String,Object>>findCustomerById(Long id);

    List<Map<String,Object>> findAllAccountsById(Long id);

    List<Map<String,Object>> selectedAccountById(List<String> accounts);

    List<Map<String,Object>> findAllTransactions(Long id,List<String> accounts, String fromDate, String toDate);

    Boolean isLinkAnalysisEnable();

}
