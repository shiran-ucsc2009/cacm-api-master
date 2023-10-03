/* PRODUCT : cacm
 * PROJECT : cacm
 * PACKAGE : com.kpmg.cacm.api.model
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
 * Date : 2/7/2020 - 3:18 PM
 * Since : version 1.0
 * Description :
 * ************* */
package com.kpmg.cacm.api.model;

import com.kpmg.cacm.api.dto.constant.IncidentApproveAction;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

/**
 * Author: ksenanayake1
 * Date : 2/7/2020
 */

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class IncidentReasons extends AbstractBaseEntity {

    private String reason;

    @ManyToOne
    private Category category;

    @Enumerated(EnumType.STRING)
    private IncidentApproveAction approveAction;


}