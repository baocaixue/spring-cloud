package com.isaac.licenses.utils;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

import static com.isaac.licenses.constant.ZuulConstant.*;

public class UserContextInterceptor implements ClientHttpRequestInterceptor {
    @Override
    public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] bytes, ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
        var headers = httpRequest.getHeaders();
        headers.add(CORRELATION_ID, UserContextHolder.getContext().getCorrelationId());
        headers.add(AUTH_TOKEN, UserContextHolder.getContext().getAuthToken());
        return clientHttpRequestExecution.execute(httpRequest, bytes);
    }
}
