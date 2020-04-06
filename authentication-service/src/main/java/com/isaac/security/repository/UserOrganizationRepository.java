package com.isaac.security.repository;

import com.isaac.security.model.UserOrganization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserOrganizationRepository extends JpaRepository<UserOrganization, String> {
    Optional<UserOrganization> findByUserName(String userName);
}
