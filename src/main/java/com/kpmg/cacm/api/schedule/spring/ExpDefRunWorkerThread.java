package com.kpmg.cacm.api.schedule.spring;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kpmg.cacm.api.model.ExceptionDefinitionRun;
import com.kpmg.cacm.api.model.ExceptionDefinitionRunError;
import com.kpmg.cacm.api.service.ExceptionDefinitionRunErrorService;
import com.kpmg.cacm.api.service.ExceptionDefinitionRunService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExpDefRunWorkerThread implements Runnable {

    private final ExceptionDefinitionRun exceptionDefRun;

    private final ExceptionDefinitionRunService expDefRunService;

    private final ExceptionDefinitionRunErrorService expDefRunErrorService;

    ExpDefRunWorkerThread(
        final ExceptionDefinitionRun exceptionDefRun,
        final ExceptionDefinitionRunService expDefRunService,
        final ExceptionDefinitionRunErrorService expDefRunErrorService) {
        this.exceptionDefRun = exceptionDefRun;
        this.expDefRunService = expDefRunService;
        this.expDefRunErrorService = expDefRunErrorService;
    }

    @Override
    public void run() {
        final String traceId = UUID.randomUUID().toString();
        try {
            ExpDefRunWorkerThread.log.info(
                "[{}] Start Running Exception Definition-{}",
                traceId,
                this.exceptionDefRun.getExceptionDefinitionId()
            );
            this.expDefRunService.run(this.exceptionDefRun, traceId);
            ExpDefRunWorkerThread.log.info(
                "[{}] Finished Running Exception Definition-{}",
                traceId,
                this.exceptionDefRun.getExceptionDefinitionId()
            );
        } catch (final JsonProcessingException | RuntimeException exception) {
            final StringWriter stringWriter = new StringWriter();
            exception.printStackTrace(new PrintWriter(stringWriter));

            final ExceptionDefinitionRunError expDefRunError = new ExceptionDefinitionRunError();
            expDefRunError.setExceptionDefinitionRun(this.exceptionDefRun);
            expDefRunError.setTraceId(traceId);
            expDefRunError.setStackTrace(stringWriter.toString());
            this.expDefRunErrorService.save(expDefRunError);

            ExpDefRunWorkerThread.log.error(
                "[{}] Error running Exception Definition-{}; {}",
                traceId,
                this.exceptionDefRun.getExceptionDefinitionId(),
                exception.getLocalizedMessage(),
                exception
            );
        }
    }
}
