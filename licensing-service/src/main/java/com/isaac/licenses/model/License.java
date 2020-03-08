package com.isaac.licenses.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class License {
    private String id;
    private String organizationId;
    private String productName;
    private String licenseType;
}
