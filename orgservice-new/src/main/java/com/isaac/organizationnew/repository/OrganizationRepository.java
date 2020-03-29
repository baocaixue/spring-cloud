package com.isaac.organizationnew.repository;

import com.isaac.organizationnew.model.Organization;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface OrganizationRepository extends CrudRepository<Organization, String> {
    Optional<Organization> findById(String id);
}
