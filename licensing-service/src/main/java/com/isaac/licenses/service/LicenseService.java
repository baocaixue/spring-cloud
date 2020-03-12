package com.isaac.licenses.service;

import com.isaac.licenses.client.OrganizationDiscoveryClient;
import com.isaac.licenses.client.OrganizationFeignClient;
import com.isaac.licenses.client.OrganizationRestTemplateClient;
import com.isaac.licenses.config.ServiceConfig;
import com.isaac.licenses.model.License;
import com.isaac.licenses.model.Organization;
import com.isaac.licenses.repository.LicenseRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class LicenseService {
    private LicenseRepository licenseRepository;
    private ServiceConfig serviceConfig;
    private OrganizationDiscoveryClient organizationDiscoveryClient;
    private OrganizationFeignClient organizationFeignClient;
    private OrganizationRestTemplateClient organizationRestTemplateClient;

    public License getLicense(String organizationId,String licenseId, String clientType) {
//        return new License()
//                .setId(licenseId)
//                .setOrganizationId(organizationId)
//                .setProductName("Test Product Name")
//                .setLicenseType("PerSeat");
        return licenseRepository.findByOrganizationIdAndId(organizationId, licenseId)
                .map(license -> license.setOrganization(this.retrieveOrgInfo(license.getOrganizationId(), clientType)))
                .orElse(null);
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

    public List<License> getLicensesByOrg(String organizationId){
        return licenseRepository.findByOrganizationId( organizationId );
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
