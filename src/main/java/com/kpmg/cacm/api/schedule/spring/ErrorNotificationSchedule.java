package com.kpmg.cacm.api.schedule.spring;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import com.kpmg.cacm.api.model.ExceptionDefinitionRunError;
import com.kpmg.cacm.api.service.EmailPropertyService;
import com.kpmg.cacm.api.service.EmailService;
import com.kpmg.cacm.api.service.ExceptionDefinitionRunErrorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ErrorNotificationSchedule {

    private final EmailPropertyService emailPropertyService;

    private final EmailService emailService;

    private final ExceptionDefinitionRunErrorService expDefRunErrorService;

    @Autowired
    public ErrorNotificationSchedule(
            final EmailPropertyService emailPropertyService,
            final EmailService emailService,
            final ExceptionDefinitionRunErrorService expDefRunErrorService) {
        this.emailPropertyService = emailPropertyService;
        this.emailService = emailService;
        this.expDefRunErrorService = expDefRunErrorService;
    }

    @Scheduled(cron = "0 0 6 * * ?", zone = "IST") //Everyday 6.00AM
    public void sendErrorNotificationEmail() {
        final List<ExceptionDefinitionRunError> expDefRunErrors = this.expDefRunErrorService.findAllByCreationTimestampAfter(
            Date.from(LocalDateTime
                .of(LocalDate.now(), LocalTime.MIDNIGHT)
                .atZone(ZoneId.systemDefault())
                .toInstant()
            )
        );
        if (!expDefRunErrors.isEmpty()) {
            this.emailService.sendErrorNotificationScheduleEmail(
                expDefRunErrors,
                this.emailPropertyService.findByName("ERROR_NOTIFICATION_SCHEDULE")
            );
        }
    }
}
