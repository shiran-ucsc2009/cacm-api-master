package com.kpmg.cacm.api.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)

public class ReportingOfficer extends AbstractBaseEntity {

    @NotNull
    private String name;

    @NotNull
    private String designation;

    @NotNull
    private String address;

    private String telephone;

    private String fax;

    private String email;


}
