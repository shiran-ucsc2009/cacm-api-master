/* PRODUCT : complete
 * PROJECT : complete
 * PACKAGE : com.example.authenticatingldap
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
 * Author : sdhanasena
 * Date : 7/30/2020 - 10:41 AM
 * Since : version 1.0
 * Description :
 * ************* */
package com.kpmg.cacm.api.ad;

import lombok.Data;

/**
 * Author: sdhanasena
 * Date : 7/30/2020
 */
@Data
public class AdDto {

    private String userName;
    private String mail;
    private String employeeNo;
    private String mobile;
    private String displayName;
    private String description;
    private String department;

}