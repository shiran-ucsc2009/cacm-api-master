package com.kpmg.cacm.api.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class PrivilegeMapping extends AbstractBaseEntity {

    @NotNull
    @ManyToOne
    private UserGroup userGroup;

    @NotNull
    @ManyToOne
    private Privilege privilege;

    private boolean enabled;

}
