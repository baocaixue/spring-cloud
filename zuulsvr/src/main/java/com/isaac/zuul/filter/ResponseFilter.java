package com.isaac.zuul.filter;

import com.isaac.zuul.utils.FilterUtils;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.isaac.zuul.constant.ZuulConstant.*;

/**
 * 后置过滤器
 */
@Component
@Slf4j
@AllArgsConstructor
public class ResponseFilter extends ZuulFilter {
    private FilterUtils filterUtils;

    @Override
    public String filterType() {
        return POST_FILTER_TYPE;
    }

    @Override
    public int filterOrder() {
        return FILTER_ORDER;
    }

    @Override
    public boolean shouldFilter() {
        return SHOULD_FILTER;
    }

    @Override
    public Object run() throws ZuulException {
        var ctx = RequestContext.getCurrentContext();
        String correlationId = filterUtils.getCorrelationId();
        log.debug("Adding the correlation id to the outbound headers. {}", correlationId);
        ctx.getResponse().addHeader(CORRELATION_ID, correlationId);
        log.debug("Completing outgoing request for {}.", ctx.getRequest().getRequestURI());
        return null;
    }
}
