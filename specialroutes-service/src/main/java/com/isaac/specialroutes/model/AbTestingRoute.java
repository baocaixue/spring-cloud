package com.isaac.specialroutes.model;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "abtesting")
@Data
@Accessors(chain = true)
public class AbTestingRoute {
    @Id
    @Column(name = "service_name", nullable = false)
    String serviceName;

    @Column(name="active", nullable = false)
    String active;

    @Column(name = "endpoint", nullable = false)
    String endpoint;

    @Column(name = "weight", nullable = false)
    Integer weight;
}
