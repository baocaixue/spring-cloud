package com.isaac.specialroutes.controller;

import com.isaac.specialroutes.model.AbTestingRoute;
import com.isaac.specialroutes.service.AbTestingRouteService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "v1/route/")
@AllArgsConstructor
public class SpecialRoutesServiceController {
    private AbTestingRouteService abTestingRouteService;

    @GetMapping(value="abtesting/{serviceName}")
    public AbTestingRoute abstestings(@PathVariable("serviceName") String serviceName) {

        return abTestingRouteService.getRoute( serviceName);
    }
}
