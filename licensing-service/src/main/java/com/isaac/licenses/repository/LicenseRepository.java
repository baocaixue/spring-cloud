package com.isaac.licenses.repository;

import com.isaac.licenses.model.License;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LicenseRepository extends CrudRepository<License, String> {
    List<License> findByOrganizationId(String organizationId);
    License findByOrganizationIdAndId(String organizationId, String licenseId);
}
