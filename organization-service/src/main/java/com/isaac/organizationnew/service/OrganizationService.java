package com.isaac.organizationnew.service;

import com.isaac.organizationnew.events.source.SimpleSourceBean;
import com.isaac.organizationnew.model.Organization;
import com.isaac.organizationnew.repository.OrganizationRepository;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class OrganizationService {
    private OrganizationRepository orgRepository;
    private SimpleSourceBean simpleSourceBean;

    public Organization getOrg(String organizationId) {
        return orgRepository.findById(organizationId).orElse(null);
    }

    @HystrixCommand
    public void saveOrg(Organization org){
        org.setId( UUID.randomUUID().toString());

        orgRepository.save(org);

        simpleSourceBean.publishOrgChange("SAVE", org.getId());
    }

    public void updateOrg(Organization org){
        orgRepository.save(org);
    }

    public void deleteOrg(String organizationId){
        orgRepository.deleteById(organizationId);
    }
}
