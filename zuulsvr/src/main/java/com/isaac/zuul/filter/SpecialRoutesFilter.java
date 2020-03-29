package com.isaac.zuul.filter;

import com.isaac.zuul.model.AbTestingRoute;
import com.isaac.zuul.utils.FilterUtils;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpRequest;
import org.springframework.cloud.netflix.zuul.filters.ProxyRequestHelper;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import static com.isaac.zuul.constant.ZuulConstant.*;

@AllArgsConstructor
@Component
@Slf4j
public class SpecialRoutesFilter extends ZuulFilter {
    private FilterUtils filterUtils;
    private RestTemplate restTemplate;

    private ProxyRequestHelper helper;

    @Override
    public String filterType() {
        return ROUTE_FILTER_TYPE;
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
        var abTestRoute = getAbRoutingInfo(filterUtils.getServiceId());
        if (abTestRoute != null && userSpecialRoute(abTestRoute)) {
            var route = buildRouteString(ctx.getRequest().getRequestURI(), abTestRoute.getEndpoint(), ctx.get(SERVICE_ID).toString());
            route = "http://localhost:8087/v1/organizations/e254f8c-c442-4ebe-a82a-e2fc1d1ff78a";
            forwardToSpecialRoute(route);
        }
        return null;
    }

    private void forwardToSpecialRoute(String route) {
        var context = RequestContext.getCurrentContext();
        var request = context.getRequest();

        var headers = this.helper.buildZuulRequestHeaders(request);
        var params = this.helper.buildZuulRequestQueryParams(request);
        var verb = getVerb(request);
        var requestEntity = getRequestBody(request);
        if (request.getContentLength() < 0) {
            context.setChunkedRequestBody();
        }

        this.helper.addIgnoredHeaders();
        CloseableHttpClient httpClient = null;
        HttpResponse response;

        try {
            httpClient  = HttpClients.createDefault();
            response = forward(httpClient, verb, route, request, headers,
                    params, requestEntity);
            setResponse(response);
        }
        catch (Exception ex ) {
            ex.printStackTrace();

        }
        finally{
            try {
                httpClient.close();
            }
            catch(IOException ex){}
        }
    }

    private void setResponse(HttpResponse response) throws IOException {
        this.helper.setResponse(response.getStatusLine().getStatusCode(),
                response.getEntity() == null ? null : response.getEntity().getContent(),
                revertHeaders(response.getAllHeaders()));
    }

    private MultiValueMap<String, String> revertHeaders(Header[] headers) {
        var map = new LinkedMultiValueMap<String, String>();
        for (Header header : headers) {
            String name = header.getName();
            if (!map.containsKey(name)) {
                map.put(name, new ArrayList<>());
            }
            Objects.requireNonNull(map.get(name)).add(header.getValue());
        }
        return map;
    }

    private HttpResponse forward(HttpClient httpclient, String verb, String uri,
                                 HttpServletRequest request, MultiValueMap<String, String> headers,
                                 MultiValueMap<String, String> params, InputStream requestEntity)
            throws Exception {
        var info = this.helper.debug(verb, uri, headers, params,
                requestEntity);
        var host = new URL( uri );
        var httpHost = getHttpHost(host);

        HttpRequest httpRequest;
        var contentLength = request.getContentLength();
        var entity = new InputStreamEntity(requestEntity, contentLength,
                request.getContentType() != null
                        ? ContentType.create(request.getContentType()) : null);
        switch (verb.toUpperCase()) {
            case "POST":
                var httpPost = new HttpPost(uri);
                httpRequest = httpPost;
                httpPost.setEntity(entity);
                break;
            case "PUT":
                var httpPut = new HttpPut(uri);
                httpRequest = httpPut;
                httpPut.setEntity(entity);
                break;
            case "PATCH":
                var httpPatch = new HttpPatch(uri );
                httpRequest = httpPatch;
                httpPatch.setEntity(entity);
                break;
            default:
                httpRequest = new BasicHttpRequest(verb, uri);

        }
        try {
            httpRequest.setHeaders(convertHeaders(headers));

            return forwardRequest(httpclient, httpHost, httpRequest);
        }
        finally {
        }
    }

    private HttpHost getHttpHost(URL host) {
        return new HttpHost(host.getHost(), host.getPort(),
                host.getProtocol());
    }

    private Header[] convertHeaders(MultiValueMap<String, String> headers) {
        var list = new ArrayList<Header>();
        for (String name : headers.keySet()) {
            for (String value : headers.get(name)) {
                list.add(new BasicHeader(name, value));
            }
        }
        return list.toArray(new Header[0]);
    }

    private HttpResponse forwardRequest(HttpClient httpclient, HttpHost httpHost,
                                        HttpRequest httpRequest) throws IOException {
        return httpclient.execute(httpHost, httpRequest);
    }

    private InputStream getRequestBody(HttpServletRequest request) {
        InputStream requestEntity = null;
        try {
            requestEntity = request.getInputStream();
        }
        catch (IOException ex) {
            // no requestBody is ok.
        }
        return requestEntity;
    }

    private String getVerb(HttpServletRequest request) {
        var sMethod = request.getMethod();
        return sMethod.toUpperCase();
    }

    private String buildRouteString(String oldEndpoint, String newEndpoint, String serviceName){
        var index = oldEndpoint.indexOf(serviceName);

        var strippedRoute = oldEndpoint.substring(index + serviceName.length());
        log.info("Target route: " + String.format("%s/%s", newEndpoint, strippedRoute));
        return String.format("%s/%s", newEndpoint, strippedRoute);
    }

    private boolean userSpecialRoute(AbTestingRoute abTestRoute) {
//        return true;
        var random = new Random();
        if (abTestRoute.getActive().equals("N"))
            return false;
        int value = random.nextInt(10) + 1;
        return abTestRoute.getWeight() < value;
    }

    private AbTestingRoute getAbRoutingInfo(String serviceId) {
        ResponseEntity<AbTestingRoute> restExchange;
        try {
            restExchange = restTemplate.exchange(
                    "http://specialroutesservice/v1/route/abtesting/{serviceName}",
                    HttpMethod.GET, null, AbTestingRoute.class, serviceId
            );
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND)
                return null;
            throw e;

        }
        return restExchange.getBody();
    }
}
