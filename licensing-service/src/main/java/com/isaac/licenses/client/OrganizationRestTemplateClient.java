package com.isaac.licenses.client;

import com.isaac.licenses.config.MicroServiceProperties;
import com.isaac.licenses.model.Organization;
import com.isaac.licenses.repository.OrganizationRedisRepository;
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
    private OrganizationRedisRepository orgRedisRepo;

    private Organization checkRedisCache(String organizationId) {
        try {
            return orgRedisRepo.findOrganization(organizationId);
        } catch (Exception e) {
            log.error("Error encountered while trying to retrieve organization {} check Redis Cache.Exception {}",
                    organizationId, e);
            return null;
        }
    }

    private void cacheOrganizationObject(Organization org) {
        try {
            orgRedisRepo.saveOrganization(org);
        } catch (Exception e) {
            log.error("Unable to cache organization {} in Redis. exception {}", org.getId(), e);
        }
    }

    public Organization getOrganization(String organizationId) {
        var org = checkRedisCache(organizationId);
        if (org != null) {
            log.debug("successfully retrieved an organization {} from the Redis cache {}", organizationId, org);
            return org;
        }
        log.debug("Unable to locate organization from the Redis cache: {}", organizationId);

        var retrieveOrganizationUrl = microServiceProperties.getRetrieveOrganizationUrl();
        try {
            var restExchange = restTemplate.exchange(
                    retrieveOrganizationUrl,
                    HttpMethod.GET,
                    null, Organization.class, organizationId);
            org = restExchange.getBody();

            if (org != null) {
                cacheOrganizationObject(org);
            }
            return org;
        } catch (Exception e) {
            log.error("Sorry, call Organization Service failed by URL: {}, and Failed message is {}",retrieveOrganizationUrl, e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
