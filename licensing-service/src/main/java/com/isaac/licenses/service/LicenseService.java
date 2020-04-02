package com.isaac.licenses.service;

import com.isaac.licenses.client.OrganizationDiscoveryClient;
import com.isaac.licenses.client.OrganizationFeignClient;
import com.isaac.licenses.client.OrganizationRestTemplateClient;
import com.isaac.licenses.config.ServiceConfig;
import com.isaac.licenses.model.License;
import com.isaac.licenses.model.Organization;
import com.isaac.licenses.repository.LicenseRepository;
import com.isaac.licenses.utils.UserContextHolder;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class LicenseService {
    private LicenseRepository licenseRepository;
    private ServiceConfig serviceConfig;
    private OrganizationDiscoveryClient organizationDiscoveryClient;
    private OrganizationFeignClient organizationFeignClient;
    private OrganizationRestTemplateClient organizationRestTemplateClient;

    //Hystrix的配置可以都放在Spring Cloud Config
    @HystrixCommand(
            fallbackMethod = "buildFallbackLicense",
            threadPoolKey = "getLicenseThreadPool",//线程池名称
            //底层线程池行为配置
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize", value = "30"),//线程池最大数量
                    @HystrixProperty(name = "maxQueueSize", value = "10")//位于线程池前的队列，可以对传入的请求排队
            },
            //定制与Hystrix命令关联的断路器的行为
            commandProperties = {
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),//控制Hystrix考虑将该断路器跳闸前，在10s之内必须发生的连续调用数量
                    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "75"),//超过第一个值后在断路器跳闸前必须达到的调用失败百分比
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "7000"),//断路器跳闸后，Hystrix允许另一个调用通过以便查看服务是否健康之前Hystrix的休眠时间
                    @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "15000"),//控制Hystrix用来监视服务调用问题的窗口大小，默认10000ms
                    @HystrixProperty(name = "metrics.rollingStats.numBuckets", value = "5")//定义滚动窗口中收集统计信息的次数，要能被前一个整除
                    //这里表示Hystrix将使用15s的窗口，并将统计信息收集到长度为3s的5个bucket中
            }
    )
    public License getLicense(String organizationId,String licenseId, String clientType) {
//        return new License()
//                .setId(licenseId)
//                .setOrganizationId(organizationId)
//                .setProductName("Test Product Name")
//                .setLicenseType("PerSeat");
        log.info("LicenseService Correlation id: {}", UserContextHolder.getContext().getCorrelationId());
        //randomlyRunLong();//模拟服务调用超时
        return licenseRepository.findByOrganizationIdAndId(organizationId, licenseId)
                .map(license -> license.setOrganization(this.retrieveOrgInfo(license.getOrganizationId(), clientType)))
                .orElse(null);
    }

    //fallback strategy, 方法签名要完全相同
    private License buildFallbackLicense(String organizationId, String licenseId, String clientType) {
        return new License().setId(licenseId)
                .setOrganizationId(organizationId)
                .setProductName("Sorry no License");
    }

    public void saveLicense(License license) {
        license.setId(UUID.randomUUID().toString());
        licenseRepository.save(license);
    }

    public void updateLicense(License license) {
        licenseRepository.save(license);
    }

    public void deleteLicense(License license) {
        licenseRepository.delete(license);
    }

    @HystrixCommand(
            commandProperties = {
                    @HystrixProperty(
                            name = "execution.isolation.thread.timeoutInMilliseconds",
                            value = "5000"
                    )
            }
    )//应尽量避免设置超时时间，如果无法解决运行缓慢的服务调用，务必将这些服务调用隔离到单独的线程池中
    public List<License> getLicensesByOrg(String organizationId){
        //randomlyRunLong();//模拟数据库超时场景
        return licenseRepository.findByOrganizationId( organizationId );
    }

    private void randomlyRunLong() {
        Random random = new Random();
        int randomNum = random.nextInt(3) + 1;
        if (randomNum == 3) sleep();
    }

    private void sleep() {
        try {
            Thread.sleep(1000 * 10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private Organization retrieveOrgInfo(String organizationId, String clientType) {
        Organization organization;
        switch (clientType) {
            case "rest":
                organization = organizationRestTemplateClient.getOrganization(organizationId);
                break;
            case "discovery":
                organization = organizationDiscoveryClient.getOrganization(organizationId);
                break;
            case "feign":
                organization = organizationFeignClient.getOrganization(organizationId);
                break;
            default:
                organization = organizationRestTemplateClient.getOrganization(organizationId);
        }
        return organization;
    }
}
