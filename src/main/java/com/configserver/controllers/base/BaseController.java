package com.configserver.controllers.base;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Pipeline;
import com.configserver.dtos.base.AResponseBase;
import com.configserver.dtos.responses.ResponseImpl;
import com.configserver.services.tracing.CorrelationService;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseController {

    @Autowired
    private Pipeline pipeline;

    @Autowired
    private CorrelationService correlationService;


    public ResponseImpl<AResponseBase> mediate( Command<AResponseBase> command ) {
        return ResponseImpl.Success(command.execute(pipeline), correlationService.getCorrelationId());
    }

}
