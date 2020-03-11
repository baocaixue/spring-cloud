package com.isaac.organization.controller;

import com.isaac.organization.model.Organization;
import com.isaac.organization.service.OrganizationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping(value="v1/organizations")
public class OrganizationController {
    private OrganizationService orgService;

    @GetMapping(value="/{organizationId}")
    public Organization getOrganization(@PathVariable("organizationId") String organizationId) {
        return orgService.getOrg(organizationId);
    }

    @PutMapping
    public void updateOrganization(@RequestBody Organization org) {
        orgService.updateOrg( org );
    }

    @PostMapping
    public void saveOrganization(@RequestBody Organization org) {
        orgService.saveOrg( org );
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrganization(@RequestBody Organization org) {
        orgService.deleteOrg( org );
    }
}
