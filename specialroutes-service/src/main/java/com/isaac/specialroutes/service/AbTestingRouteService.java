package com.isaac.specialroutes.service;

import com.isaac.specialroutes.exception.NoRouteFoundException;
import com.isaac.specialroutes.model.AbTestingRoute;
import com.isaac.specialroutes.repository.AbTestingRouteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AbTestingRouteService {
    private AbTestingRouteRepository abTestingRouteRepository;

    public AbTestingRoute getRoute(String serviceName) throws NoRouteFoundException {
        return abTestingRouteRepository.findByServiceName(serviceName).orElseThrow(NoRouteFoundException::new);
    }

    public void saveAbTestingRoute(AbTestingRoute abTestingRoute) {
        abTestingRouteRepository.save(abTestingRoute);
    }

    public void deleteRoute(AbTestingRoute abTestingRoute) {
        abTestingRouteRepository.delete(abTestingRoute);
    }
}
