package com.isaac.licenses.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.stereotype.Component;

import javax.persistence.*;

@Data
@Accessors(chain = true)
@Entity
@Table(name = "licenses")
public class License {
    @Id
    @Column(name = "license_id")
    private String id;

    @Column(name = "organization_id")
    private String organizationId;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "license_type")
    private String licenseType;

    @Column(name = "license_max", nullable = false)
    private Integer licenseMax;

    @Column(name = "license_allocated", nullable = false)
    private Integer licenseAllocated;

    @Column(name="comment")
    private String comment;

    @Transient
    private Organization organization;
}
