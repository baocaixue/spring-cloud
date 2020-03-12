package com.isaac.licenses.client;


import com.isaac.licenses.model.Organization;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Feign客户端使用Java接口及注解来调用服务
 */
@FeignClient("organizationservice")
public interface OrganizationFeignClient {
    @GetMapping(value = "/v1/organizations/{organizationId}", consumes = "application/json")
    Organization getOrganization(@PathVariable String organizationId);
}
