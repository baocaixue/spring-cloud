package com.isaac.licenses.service;

import com.isaac.licenses.model.License;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class LicenseService {
    public License getLicense(String organizationId,String licenseId) {
        return new License()
                .setId(licenseId)
                .setOrganizationId(organizationId)
                .setProductName("Test Product Name")
                .setLicenseType("PerSeat");
    }

    public void saveLicense(License license) {}

    public void updateLicense(License license) {}

    public void deleteLicense(License license) {}
}
