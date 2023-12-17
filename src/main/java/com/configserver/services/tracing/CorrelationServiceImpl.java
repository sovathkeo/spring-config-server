package com.configserver.services.tracing;

import com.configserver.common.constant.HttpHeaderConstant;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.UUID;

@Service
public class CorrelationServiceImpl implements CorrelationService{

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Override
    public String getCorrelationId() {

        RequestAttributes attr = RequestContextHolder.getRequestAttributes();

        if (attr != null) {

            HttpServletRequest request = ((ServletRequestAttributes) attr).getRequest();

            var correlationId = request.getAttribute(HttpHeaderConstant.CORRELATION_ID);

            if (correlationId != null && StringUtils.hasText(correlationId.toString())) {
                return correlationId.toString();
            }
        }

        return UUID.randomUUID().toString();
    }
}
