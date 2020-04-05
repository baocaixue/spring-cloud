package com.isaac.licenses.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "services")
public class MicroServiceProperties {
    private String retrieveOrganizationUrl;
}
