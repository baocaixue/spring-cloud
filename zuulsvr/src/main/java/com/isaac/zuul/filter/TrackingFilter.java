package com.isaac.zuul.filter;

import com.isaac.zuul.constant.ZuulConstant;
import com.isaac.zuul.utils.FilterUtils;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

/**
 * zuul前置过滤器
 */
@Component
@Slf4j
@AllArgsConstructor
public class TrackingFilter extends ZuulFilter {
    private FilterUtils filterUtils;

    @Override
    public String filterType() {
        return ZuulConstant.PRE_FILTER_TYPE;
    }

    @Override
    public int filterOrder() {
        return ZuulConstant.FILTER_ORDER;
    }

    @Override
    public boolean shouldFilter() {
        return ZuulConstant.SHOULD_FILTER;
    }

    @Override
    public Object run() throws ZuulException {
        if (isCorrelationIdPresent()) {
            log.debug("tmx-correlation-id found in tracking filter: {}. ", filterUtils.getCorrelationId());
        } else {
            filterUtils.setCorrelationId(generateCorrelationId());
            log.debug("tmx-correlation-id generated in tracking filter: {}.", filterUtils.getCorrelationId());
        }
        var ctx = RequestContext.getCurrentContext();
        log.debug("Processing incoming request for {}.",  ctx.getRequest().getRequestURI());
        return null;
    }

    private String generateCorrelationId() {
        return UUID.randomUUID().toString();
    }

    private boolean isCorrelationIdPresent() {
       return Optional.ofNullable(filterUtils.getCorrelationId()).isPresent();
    }
}
