/* PRODUCT : cacm
 * PROJECT : cacm
 * PACKAGE : com.kpmg.cacm.api.schedule.spring
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
 * Date : 2/17/2020 - 11:36 AM
 * Since : version 1.0
 * Description :
 * ************* */
package com.kpmg.cacm.api.schedule.spring;

import com.kpmg.cacm.api.dto.response.model.IncidentEmailDTO;
import com.kpmg.cacm.api.service.EmailService;
import com.kpmg.cacm.api.service.IncidentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Author: ksenanayake1
 * Date : 2/17/2020
 */
@Component
@Slf4j
public class IncidentNotificationSchedule {

    private final IncidentService incidentService;

    private final EmailService emailService;

    public IncidentNotificationSchedule(IncidentService incidentService, EmailService emailService) {
        this.incidentService = incidentService;
        this.emailService = emailService;
    }

    @Scheduled(cron = "0 45 9 * * ?", zone = "IST") //Everyday 9.45AM
    public void sendIncidentNotificationEmail(){

        List<IncidentEmailDTO> incidentEmailDTOS = this.incidentService.findAllByCategoryAndAssigneeAndCreationDate();
        incidentEmailDTOS.addAll(this.incidentService.findAllByCategoryAndOwnerAndCreationDate());


       if(!incidentEmailDTOS.isEmpty()){
           this.emailService.sendIncidentNotificationScheduleEmail(incidentEmailDTOS);
       }

   }

}