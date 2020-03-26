package com.isaac.zuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableZuulProxy//使一个服务成为Zuul服务器
/**
 * 注：@EnableZuulServer注解创建打服务器，它不会加载任何Zuul反向代理过滤器，也不会使用Netflix Eureka进行服务发现;
 *
 * 如果想要构建自己打路由服务，而不使用任何Zuul预置的功能时会使用@EnableZuulServer，例如，需要使用Zuul与Eureka之外
 * 的其他服务发现引擎（如Consul）进行集成打是哈
 */
public class ZuulServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZuulServerApplication.class, args);
    }
}
