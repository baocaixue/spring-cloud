package com.isaac.licenses.client;

import com.isaac.licenses.config.MicroServiceProperties;
import com.isaac.licenses.model.Organization;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class OrganizationRestTemplateClient {
    //private RestTemplate restTemplate;
    private OAuth2RestTemplate restTemplate;
    private MicroServiceProperties microServiceProperties;

    public Organization getOrganization(String organizationId) {
        var retrieveOrganizationUrl = microServiceProperties.getRetrieveOrganizationUrl();
        try {
            var restExchange = restTemplate.exchange(
                    retrieveOrganizationUrl,
                    HttpMethod.GET,
                    null, Organization.class, organizationId);
            return restExchange.getBody();
        } catch (Exception e) {
            log.error("Sorry, call Organization Service failed by URL: {}, and Failed message is {}",retrieveOrganizationUrl, e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
