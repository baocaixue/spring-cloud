package com.isaac.licenses.service;

import com.isaac.licenses.config.ServiceConfig;
import com.isaac.licenses.model.License;
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

    public License getLicense(String organizationId,String licenseId) {
//        return new License()
//                .setId(licenseId)
//                .setOrganizationId(organizationId)
//                .setProductName("Test Product Name")
//                .setLicenseType("PerSeat");
        return licenseRepository.findByOrganizationIdAndId(organizationId, licenseId);
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
}
