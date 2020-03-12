package com.isaac.licenses.client;

import com.isaac.licenses.model.Organization;
import lombok.AllArgsConstructor;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * 使用DiscoveryClient检索服务端点来进行调用
 */
@Component
@AllArgsConstructor
public class OrganizationDiscoveryClient {
    private static final RestTemplate restTemplate = new RestTemplate();
    private DiscoveryClient discoveryClient;

    public Organization getOrganization(String organizationId) {
        var instances = discoveryClient.getInstances("organizationservice");
        if (instances.size() == 0) {
            return null;
        }
        var serviceUri = String.format("%s/v1/organizations/%s", instances.get(0).getUri().toString(), organizationId);
        var restExchange = restTemplate.exchange(
                serviceUri,
                HttpMethod.GET,
                null, Organization.class, organizationId);
        return restExchange.getBody();
    }
}
