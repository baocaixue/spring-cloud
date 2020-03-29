package com.isaac.licenses.utils;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class UserContext {
    private String correlationId= "";
    private String authToken= "";
    private String userId = "";
    private String orgId = "";
}
