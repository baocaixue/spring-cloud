package com.isaac.specialroutes.repository;

import com.isaac.specialroutes.model.AbTestingRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AbTestingRouteRepository extends JpaRepository<AbTestingRoute, String> {
    Optional<AbTestingRoute> findByServiceName(String serviceName);
}
