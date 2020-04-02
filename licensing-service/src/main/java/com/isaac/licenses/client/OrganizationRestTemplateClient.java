package com.isaac.licenses.client;

import com.isaac.licenses.model.Organization;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * 带有Ribbon功能的RestTemplate
 * 用服务ID构建目标URL
 */
@Component
@AllArgsConstructor
public class OrganizationRestTemplateClient {
    //private RestTemplate restTemplate;
    private OAuth2RestTemplate restTemplate;

    public Organization getOrganization(String organizationId) {
        var restExchange = restTemplate.exchange(
                "http://zuulservice/api/organization/v1/organizations/{organizationId}",
                HttpMethod.GET,
                null, Organization.class, organizationId);
        return restExchange.getBody();
    }
}
