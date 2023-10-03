/* PRODUCT : cacm
 * PROJECT : cacm
 * PACKAGE : com.kpmg.cacm.api.repository.spring
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
 * Date : 2/10/2020 - 8:53 AM
 * Since : version 1.0
 * Description :
 * ************* */
package com.kpmg.cacm.api.repository.spring;

import com.kpmg.cacm.api.dto.constant.IncidentApproveAction;
import com.kpmg.cacm.api.model.Category;
import com.kpmg.cacm.api.model.IncidentReasons;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Author: ksenanayake1
 * Date : 2/10/2020
 */

@Repository
public interface IncidentReasonsRepository extends JpaRepository<IncidentReasons,Long> {

    List<IncidentReasons> findAllByApproveActionAndCategoryAndDeletedFalse(IncidentApproveAction incidentApproveAction, Category category);

    List<IncidentReasons> findAllByApproveActionAndDeletedFalse(IncidentApproveAction incidentApproveAction);

    IncidentReasons findByIdAndDeletedFalse(Long id);
}
