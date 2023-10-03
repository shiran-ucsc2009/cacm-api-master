package com.kpmg.cacm.api.schedule.spring;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.kpmg.cacm.api.dto.constant.ScheduleFrequency;
import com.kpmg.cacm.api.model.ExceptionDefinition;
import com.kpmg.cacm.api.model.ExceptionDefinitionRun;
import com.kpmg.cacm.api.service.ExceptionDefinitionRunErrorService;
import com.kpmg.cacm.api.service.ExceptionDefinitionRunService;
import com.kpmg.cacm.api.service.ExceptionDefinitionService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ExpDefRunSchedule {

    private final ExceptionDefinitionService expDefService;

    private final ExceptionDefinitionRunService expDefRunService;

    private final ExceptionDefinitionRunErrorService expDefRunErrorService;

    private final ModelMapper modelMapper;

    @Value("${cacm.config.schedule.thread-pool-size}")
    private int threadPoolSize;

    @Autowired
    public ExpDefRunSchedule(
            final ExceptionDefinitionService expDefService,
            final ExceptionDefinitionRunService expDefRunService,
            final ExceptionDefinitionRunErrorService expDefRunErrorService,
            final ModelMapper modelMapper) {
        this.expDefService = expDefService;
        this.expDefRunService = expDefRunService;
        this.expDefRunErrorService = expDefRunErrorService;
        this.modelMapper = modelMapper;
    }

    @Scheduled(cron = "0 0 6 * * ?", zone = "IST")
    public void runExceptionDef() {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        final List<ExceptionDefinition> exceptionDefs = this.expDefService.findAllForScheduledRun(ScheduleFrequency.DAILY);
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
            exceptionDefs.addAll(this.expDefService.findAllForScheduledRun(ScheduleFrequency.WEEKLY));
        }
        if (calendar.get(Calendar.DAY_OF_MONTH) == 1) {
            exceptionDefs.addAll(this.expDefService.findAllForScheduledRun(ScheduleFrequency.MONTHLY));
        }
        if (calendar.get(Calendar.DAY_OF_YEAR) == 1) {
            exceptionDefs.addAll(this.expDefService.findAllForScheduledRun(ScheduleFrequency.YEARLY));
        }

        final ExecutorService executorService = new ThreadPoolExecutor(
            0, this.threadPoolSize,
            60L, TimeUnit.SECONDS,
            new SynchronousQueue<>(),
            new ThreadPoolExecutor.CallerRunsPolicy()
        );
        for (final ExceptionDefinition exceptionDefinition : exceptionDefs) {
            final ExceptionDefinitionRun exceptionDefRun = this.modelMapper.map(exceptionDefinition, ExceptionDefinitionRun.class);
            exceptionDefRun.setExceptionDefinitionId(exceptionDefinition.getId());
            this.expDefRunService.save(exceptionDefRun);
            final Runnable worker = new ExpDefRunWorkerThread(exceptionDefRun, this.expDefRunService, this.expDefRunErrorService);
            executorService.submit(worker);
        }
        executorService.shutdown();
    }
}
