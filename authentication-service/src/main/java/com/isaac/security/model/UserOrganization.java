package com.isaac.security.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user_orgs")
@Data
public class UserOrganization {
    @Id
    @Column(name = "user_name")
    private String userName;

    @Column(name = "organization_id")
    private String organizationId;
}
