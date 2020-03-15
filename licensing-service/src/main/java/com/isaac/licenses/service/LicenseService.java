package com.isaac.licenses.service;

import com.isaac.licenses.client.OrganizationDiscoveryClient;
import com.isaac.licenses.client.OrganizationFeignClient;
import com.isaac.licenses.client.OrganizationRestTemplateClient;
import com.isaac.licenses.config.ServiceConfig;
import com.isaac.licenses.model.License;
import com.isaac.licenses.model.Organization;
import com.isaac.licenses.repository.LicenseRepository;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
@AllArgsConstructor
public class LicenseService {
    private LicenseRepository licenseRepository;
    private ServiceConfig serviceConfig;
    private OrganizationDiscoveryClient organizationDiscoveryClient;
    private OrganizationFeignClient organizationFeignClient;
    private OrganizationRestTemplateClient organizationRestTemplateClient;

    @HystrixCommand(
            fallbackMethod = "buildFallbackLicense",
            threadPoolKey = "getLicenseThreadPool",//线程池名称
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize", value = "30"),//线程池最大数量
                    @HystrixProperty(name = "maxQueueSize", value = "10")//位于线程池前的队列，可以对传入的请求排队
            }
    )
    public License getLicense(String organizationId,String licenseId, String clientType) {
//        return new License()
//                .setId(licenseId)
//                .setOrganizationId(organizationId)
//                .setProductName("Test Product Name")
//                .setLicenseType("PerSeat");
        randomlyRunLong();//模拟服务调用超时
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
        randomlyRunLong();//模拟数据库超时场景
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
        Organization organization = null;
        switch (clientType) {
            case "rest":
                organization = organizationRestTemplateClient.getOrganization(organizationId);
                break;
            case "discovery":
                organization = organizationDiscoveryClient.getOrganization(organizationId);
                break;
            case "feign":
            default:
                organization = organizationFeignClient.getOrganization(organizationId);
        }
        return organization;
    }
}
