package com.isaac.licenses.controller;

import com.isaac.licenses.service.DiscoveryService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "v1/tools")
@AllArgsConstructor
public class ToolsController {
    private DiscoveryService discoveryService;

    @GetMapping("/eureka/services")
    public List<String> getEurekaServices() {
        return discoveryService.getEurekaServices();
    }
}
