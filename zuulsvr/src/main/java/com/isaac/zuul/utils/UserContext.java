package com.isaac.zuul.utils;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.stereotype.Component;

@Component
@Data
@Accessors(chain = true)
public class UserContext {
    private String correlationId= "";
    private String authToken= "";
    private String userId = "";
    private String orgId = "";
}
