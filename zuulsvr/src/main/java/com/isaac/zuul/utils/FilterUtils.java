package com.isaac.zuul.utils;

import com.netflix.zuul.context.RequestContext;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

import static com.isaac.zuul.constant.ZuulConstant.*;

@Component
public class FilterUtils {
    public String getCorrelationId() {
        return getRequestHeader(CORRELATION_ID);
    }

    public void setCorrelationId(String correlationId) {
        setRequestHeader(CORRELATION_ID, correlationId);
    }

    public String getOrgId() {
        return getRequestHeader(ORG_ID);
    }

    public void setOrgId(String orgId) {
        setRequestHeader(ORG_ID, orgId);
    }

    public String getUserId() {
       return getRequestHeader(USER_ID);
    }

    public void setUserId(String userId) {
        setRequestHeader(USER_ID, userId);
    }

    public String getAuthToken() {
        return getRequestHeader(AUTH_TOKEN);
    }

    public String getServiceId() {
        var ctx = RequestContext.getCurrentContext();
        var serviceIdOpt = Optional.ofNullable(ctx.get(SERVICE_ID));
        //if using a static, none-eureka rote, it might not have service id
        return serviceIdOpt.map(Objects::toString).orElse("");
    }

    private String getRequestHeader(String headerKey) {
        var ctx = RequestContext.getCurrentContext();
        var headOpt = Optional.ofNullable(ctx.getRequest().getHeader(headerKey));
        return headOpt.orElse(ctx.getZuulRequestHeaders().get(headerKey));
    }

    private void setRequestHeader(String headerKey, String headerValue) {
        var ctx = RequestContext.getCurrentContext();
        ctx.addZuulRequestHeader(headerKey, headerValue);
    }
}
