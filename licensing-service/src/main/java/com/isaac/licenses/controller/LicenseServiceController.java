package com.isaac.licenses.controller;

import com.isaac.licenses.model.License;
import com.isaac.licenses.service.LicenseService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "v1/organizations/{organizationId}/licenses")
@AllArgsConstructor
public class LicenseServiceController {
    private LicenseService licenseService;

    @GetMapping(value = "/{licenseId}")
    public License getLicenses(@PathVariable String organizationId, @PathVariable String licenseId) {
        return licenseService.getLicense(organizationId, licenseId);
    }
}
