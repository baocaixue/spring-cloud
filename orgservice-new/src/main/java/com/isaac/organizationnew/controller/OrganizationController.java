package com.isaac.organizationnew.controller;

import com.isaac.organizationnew.model.Organization;
import com.isaac.organizationnew.service.OrganizationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping(value="v1/organizations")
public class OrganizationController {
    private OrganizationService orgService;

    @GetMapping(value="/{organizationId}")
    public Organization getOrganization(@PathVariable("organizationId") String organizationId) {
        return Optional.ofNullable(orgService.getOrg(organizationId)).map(org -> org.setContactName("New::" + org.getContactName())).orElse(null);
    }

    @PutMapping
    public void updateOrganization(@RequestBody Organization org) {
        orgService.updateOrg( org );
    }

    @PostMapping
    public void saveOrganization(@RequestBody Organization org) {
        orgService.saveOrg( org );
    }

    @DeleteMapping(value = "/{organizationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrganization(@PathVariable String organizationId) {
        orgService.deleteOrg(organizationId);
    }
}
