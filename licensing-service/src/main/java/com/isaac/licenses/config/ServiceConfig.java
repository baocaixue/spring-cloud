package com.isaac.licenses.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class ServiceConfig {
    @Value("aaatest")
    private String exampleProperty;
}
