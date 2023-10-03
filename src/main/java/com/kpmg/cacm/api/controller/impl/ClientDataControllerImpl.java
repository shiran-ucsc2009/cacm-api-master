package com.kpmg.cacm.api.controller.impl;

import com.kpmg.cacm.api.controller.ClientDataController;
import com.kpmg.cacm.api.dto.request.QueryRunRequest;
import com.kpmg.cacm.api.dto.response.RunQueryResponse;
import com.kpmg.cacm.api.service.ClientDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClientDataControllerImpl  implements ClientDataController {

    private final ClientDataService clientDataService;

    @Autowired
    public ClientDataControllerImpl(final ClientDataService clientDataService) {
        this.clientDataService = clientDataService;
    }

    @Override
    public RunQueryResponse runQuery(final QueryRunRequest queryRunRequest) {
        return RunQueryResponse.builder()
            .resultList(this.clientDataService.executeQuery(queryRunRequest.getQuery()))
            .build();
    }
}
