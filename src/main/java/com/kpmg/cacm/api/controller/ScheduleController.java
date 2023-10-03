package com.kpmg.cacm.api.controller;

import com.kpmg.cacm.api.dto.response.EmptyResponse;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public interface ScheduleController {

    @Secured("RUN_AUTOMATIC_EXCEPTION_DEF")
    @GetMapping("/schedule/run-automatic-exception-definitions")
    EmptyResponse runAutomaticExceptionDefs();
}
