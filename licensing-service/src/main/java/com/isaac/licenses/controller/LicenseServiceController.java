package com.isaac.licenses.controller;

import com.isaac.licenses.config.ServiceConfig;
import com.isaac.licenses.model.License;
import com.isaac.licenses.service.LicenseService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "v1/organizations/{organizationId}/licenses")
@AllArgsConstructor
public class LicenseServiceController {
    private LicenseService licenseService;
    private ServiceConfig serviceConfig;

    @GetMapping
    public List<License> getLicenses(@PathVariable String organizationId) {

        return licenseService.getLicensesByOrg(organizationId);
    }

    @GetMapping(value="{licenseId}")
    public License getLicenses( @PathVariable String organizationId,
                                @PathVariable String licenseId) {

        return licenseService.getLicense(organizationId,licenseId, "");
    }

    @GetMapping(value="{licenseId}/{clientType}")
    public License getLicensesWithClient(@PathVariable String organizationId,
                                         @PathVariable String licenseId,
                                         @PathVariable String clientType) {
        return licenseService.getLicense(organizationId,licenseId, clientType);
    }

    @PutMapping(value="{licenseId}")
    public String updateLicenses( @PathVariable String licenseId) {
        return "This is the put";
    }

    @PostMapping
    public void saveLicenses(@RequestBody License license) {
        licenseService.saveLicense(license);
    }

    @DeleteMapping(value="{licenseId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String deleteLicenses( @PathVariable String licenseId) {
        return "This is the Delete";
    }
}
