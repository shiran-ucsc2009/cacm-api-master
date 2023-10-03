package com.kpmg.cacm.api.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

import com.kpmg.cacm.api.dto.constant.PrivilegeType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Privilege extends AbstractBaseEntity {

    @NotNull
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    private PrivilegeType privilegeType;

}
