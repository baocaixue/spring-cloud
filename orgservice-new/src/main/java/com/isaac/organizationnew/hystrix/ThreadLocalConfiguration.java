package com.isaac.organizationnew.hystrix;

import com.netflix.hystrix.strategy.HystrixPlugins;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class ThreadLocalConfiguration {
    @Autowired(required = false)
    private HystrixConcurrencyStrategy existingConcurrencyStrategy;

    @PostConstruct
    public void init() {
        //保留现有的Hystrix插件引用
        var hystrixPlugins = HystrixPlugins.getInstance();
        var eventNotifier = hystrixPlugins.getEventNotifier();
        var metricsPublisher = hystrixPlugins.getMetricsPublisher();
        var propertiesStrategy = hystrixPlugins.getPropertiesStrategy();
        var commandExecutionHook = hystrixPlugins.getCommandExecutionHook();
        HystrixPlugins.reset();

        //使用Hystrix插件注册自定义的Hystrix并发策略
        hystrixPlugins.registerConcurrencyStrategy(new ThreadLocalAwareStrategy(existingConcurrencyStrategy));
        hystrixPlugins.registerEventNotifier(eventNotifier);
        hystrixPlugins.registerMetricsPublisher(metricsPublisher);
        hystrixPlugins.registerPropertiesStrategy(propertiesStrategy);
        hystrixPlugins.registerCommandExecutionHook(commandExecutionHook);
    }
}
