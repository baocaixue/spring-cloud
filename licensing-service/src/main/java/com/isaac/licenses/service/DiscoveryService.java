package com.isaac.licenses.service;

import lombok.AllArgsConstructor;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DiscoveryService {
//    private RestTemplate restTemplate;
    private DiscoveryClient discoveryClient;

    public List<String> getEurekaServices(){
        return discoveryClient.getServices().parallelStream()
                .map(serviceId -> discoveryClient.getInstances(serviceId))
                .flatMap(Collection::parallelStream)
                .map(serviceInstance -> String.format("%s:%s", serviceInstance.getServiceId(), serviceInstance.getUri()))
                .collect(Collectors.toList());
    }
}
