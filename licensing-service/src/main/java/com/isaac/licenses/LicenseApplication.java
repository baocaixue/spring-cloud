package com.isaac.licenses;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@RefreshScope
//@EnableEurekaClient
@EnableDiscoveryClient
@EnableFeignClients
@EnableCircuitBreaker//告诉springCloud将要为服务使用Hystrix
public class LicenseApplication {
    public static void main(String[] args) {
        SpringApplication.run(LicenseApplication.class, args);
    }

    @Bean
    @LoadBalanced//支持Ribbon的RestTemplate
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
