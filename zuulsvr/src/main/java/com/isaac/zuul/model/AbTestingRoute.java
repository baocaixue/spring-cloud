package com.isaac.zuul.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * A/B测试：用户（服务）随机使用同一个服务的两种不同版本
 */
@Data
@Accessors(chain = true)
public class AbTestingRoute {
    private String serviceName;
    private String active;
    private String endpoint;
    private Integer weight;
}
