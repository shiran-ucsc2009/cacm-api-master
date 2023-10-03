package com.kpmg.cacm.api.controller;

import com.kpmg.cacm.api.dto.request.QueryRunRequest;
import com.kpmg.cacm.api.dto.response.RunQueryResponse;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public interface ClientDataController {

    @Secured("RUN_QUERY")
    @PostMapping("/client-data/run-query")
    RunQueryResponse runQuery(@RequestBody QueryRunRequest queryRunRequest);
}
